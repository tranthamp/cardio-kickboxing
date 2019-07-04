(ns cardio-kickboxing.gen-workout
  (:require
    [cardio-kickboxing.db.core :as db]))

(def sample-workout
  {:title "Sample Workout #1"
   :rest-seconds 30
   :active-seconds 120
   :exercises nil})

;; TODO: Make this more dynamic
(defn generate-sequence [length]
  "TODO: Generate a sequence of difficulties to outline a workout"
  (interleave '(1 1 1 1 1) '(2 2 2 2 3) '(1 1 3 1 1)))

(defn get-exercises-of-difficulty
  "Takes collection of exercises and a difficulty, returns a shuffled lazy seq of exercises matching the difficulty"
  [exercises difficulty]
  (cycle (shuffle (filter #(= difficulty (:difficulty %)) exercises))))

(defn build-lazy-seqs
  "Takes collection of exercises, returns map of difficulty to lazy seq of exercises"
  [exercises]
  (let [difficulties [1 2 3]]
    (into {}
          (map (fn [diff] [diff (get-exercises-of-difficulty exercises diff)])
               difficulties))))

(defn exercises-by-sequence
  "Takes collection of exercises and a sequence of difficulties, returns a list of exercises matching the difficulties"
  [exercises diff-seq]
  (loop [workout []
         remaining diff-seq
         lazy-seqs (build-lazy-seqs exercises)
         index 0]
    (if (empty? remaining)
      workout
      (let [diff (first remaining)
            exer (assoc (first (get lazy-seqs diff)) :index index)
            next-seqs (update lazy-seqs diff rest)]
        (recur
          (conj workout exer)
          (rest remaining)
          next-seqs
          (inc index))))))

(defn gen-workout []
  (let [workout sample-workout
        length 15]
    (assoc workout :exercises (exercises-by-sequence (db/get-exercises) (generate-sequence length)))))
