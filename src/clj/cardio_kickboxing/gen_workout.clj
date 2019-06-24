(ns cardio-kickboxing.gen-workout
  (:require
    [cardio-kickboxing.db.core :as db]))

(def sample-workout
  {:title "Sample Workout #1"
   :rest-seconds 10
   :active-seconds 20
   :exercises nil})

(defn gen-workout []
  (let [workout sample-workout
        exercise-list (db/get-exercises)]
    (assoc workout :exercises exercise-list)))
