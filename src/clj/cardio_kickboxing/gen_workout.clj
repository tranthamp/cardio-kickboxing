(ns cardio-kickboxing.gen-workout
  (:require
    [cardio-kickboxing.db.core :as db]))

(def sample-workout
  {:title "Sample Workout #1"
   :rest-seconds 10
   :active-seconds 20
   :exercises nil})

(defn select-exercises []
  (let [exercise-list (db/get-exercises)]
    (take 15 (repeatedly #(rand-nth exercise-list)))))

(defn gen-workout []
  (let [workout sample-workout]
    (assoc workout :exercises (select-exercises))))
