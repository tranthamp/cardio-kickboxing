(ns cardio-kickboxing.workout
    (:require [cardio-kickboxing.speaker :as speaker]
              [reagent.core :as r]
              [ajax.core :refer [GET]]))

(def seconds-since-start (r/atom 0))
(def timer (r/atom nil))

(defn find-first [f coll]
  (first (filter f coll)))

(defn get-exercise-by-name [exercises name]
  (find-first #(= name (:name %)) exercises))

(defn current-round
  "Returns the current round, calculated using: (floor (time / round-length))"
  [workout]
  (when-let [active-seconds (:active-seconds workout)]
    (when-let [rest-seconds (:rest-seconds workout)]
      (let [round-length (+ rest-seconds active-seconds)]
        (int (Math/floor (/ @seconds-since-start round-length)))))))

(defn seconds-remaining-in-round
  "Returns the seconds remaining in the current round"
  [workout]
  (when-let [active-seconds (:active-seconds workout)]
    (when-let [rest-seconds (:rest-seconds workout)]
      (let [round-length (+ rest-seconds active-seconds)]
        (- round-length (mod @seconds-since-start round-length))))))

(defn start-timer []
  (reset! timer (js/setInterval #(swap! seconds-since-start inc) 1000)))

(defn stop-timer []
  (js/clearInterval @timer)
  (reset! timer nil))

(defn toggle-timer []
  (if (= @timer nil) (start-timer) (stop-timer)))

(defn timer-component []
  (fn []
    [:div
      "Time since start: " @seconds-since-start " seconds"]))

(defn title-component [workout]
  [:div (:title @workout)])

(defn round-component [workout]
  (when-let [round (current-round @workout)]
    [:div
      "Round: " (+ round 1)]))

(defn current-exercise [workout]
  (when-let [round (current-round @workout)]
    (let [exercises (:exercises @workout)]
      (nth exercises round))))

(defn exercise-component [workout]
  (when-let [round (current-round @workout)]
    (let [exercises (:exercises @workout)
          exercise (nth exercises round)
          exercise-name (:name exercise)]
      [:div (str exercise-name)])))

(defn callout-exercise [workout delay-remaining]
  (when (= delay-remaining 5) (speaker/say (:callout (current-exercise workout)))))

(defn callout-dispatcher [workout time-remaining]
  (let [active-seconds (:active-seconds @workout)
        midround (/ active-seconds 2)]
    (cond
      (= time-remaining active-seconds) :start
      (= time-remaining midround) :middle
      (= time-remaining 15) :fifteen
      (= time-remaining 1) :end
      :else :none)))

(defmulti callout-times callout-dispatcher)
(defmethod callout-times :start [] (speaker/say "start"))
(defmethod callout-times :middle [workout] (when (:switch (current-exercise workout)) (speaker/say "switch")))
(defmethod callout-times :fifteen [] (speaker/say "fifteen"))
(defmethod callout-times :end [] (speaker/say "time"))
(defmethod callout-times :none [])

(defn countdown-component [workout]
  (let [active-seconds (:active-seconds @workout)
        round-remaining (seconds-remaining-in-round @workout)
        delay-remaining (- round-remaining active-seconds)]
    (if (> round-remaining active-seconds)
      [:div
        (callout-exercise workout delay-remaining)
        "Round starting in " delay-remaining " seconds"]
      [:div
        (callout-times workout round-remaining)
        "Time remaining: " round-remaining " seconds"])))

(defn start-button-component []
  [:div
    [:input {:type "button" :value "Start" :on-click toggle-timer}]])

(defn exercise-list-component [workout]
  (let [exercises (:exercises @workout)]
    [:div
      "Exercises:"
      [:ol {:style {:text-align "left"}}
        (for [exercise exercises]
          ^{:key exercise} [:li (:name exercise)])]]))

(defn gen-workout [workout]
  (GET "/gen-workout"
       {:headers {"Accept" "application/transit+json"}
        :handler #(reset! workout %)
        :error-handler #(.log js/console "Failed to fetch workout from server")}))

(defn debug-component [workout]
  [:div>p (str @workout)
   [:br]
   [:br]])

(defn display-workout [workout]
  [:div
    ;[debug-component workout]
    ;[timer-component]
    [title-component workout]
    [:br]
    [round-component workout]
    [exercise-component workout] [:br]
    [countdown-component workout]
    [start-button-component]
    [:br]
    [:br]
    [exercise-list-component workout]
   ])

(defn workout-app []
  (let [workout (r/atom {})]
    (gen-workout workout)
    (display-workout workout)))
