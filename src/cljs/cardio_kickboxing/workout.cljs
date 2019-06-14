(ns cardio-kickboxing.workout
    (:require [reagent.core :as r]))

; TODO: Allow workout and exercises to be edited by the user and generate workout
(def sample-exercises [{:name "Punching - Left" :targets [:upper] :difficulty 1 :callout "Punching, moving to the left"}
                       {:name "Punching - Right" :targets [:upper] :difficulty 1 :callout "Punching, moving to the right"}
                       {:name "Back Kicks" :targets [:lower] :difficulty 1 :callout "Because they work and I like them, back kicks"}
                       {:name "Fast Kicks" :targets [:lower] :difficulty 1 :callout "Fast kicks"}
                       {:name "Sliding Side Kicks" :targets [:lower] :difficulty 1 :callout "Sliding side kicks"}
                       {:name "Free Station - Left" :targets [:upper :lower] :difficulty 2 :callout "Left leg in front, free station" :switch true}
                       {:name "Free Station - Right" :targets [:upper :lower] :difficulty 2 :callout "Right leg in front, free station" :switch true}
                       {:name "Horse Stance Punching" :targets [:lower] :difficulty 1 :callout "Horse Stance Punching"}
                       {:name "Little Sister" :targets [:lower] :difficulty 3 :callout "Little sister"}
                       {:name "Front Leg Roundhouse Kicks" :targets [:lower] :difficulty 3 :callout "Front leg roundhouse kicks"}
                       {:name "In Her Honor" :targets [:upper :lower] :difficulty 3 :callout "In her honor"}])

(def sample-exercise-set-1 ["Punching - Left"
                          "Back Kicks"
                          "Free Station - Left"
                          "Punching - Right"
                          "Fast Kicks"
                          "Free Station - Right"
                          "Punching - Left"
                          "Sliding Side Kicks"
                          "Free Station - Left"
                          "Punching - Right"
                          "Little Sister"
                          "Free Station - Right"
                          "Back Kicks"
                          "In Her Honor"
                          "Horse Stance Punching"])

(def sample-workout-1
  {:title "Sample Workout #1"
   :rest-seconds 30
   :active-seconds 120
   :exercises sample-exercise-set-1})

(def seconds-since-start (r/atom 1))
(def timer (r/atom nil))

(defn find-first [f coll]
  (first (filter f coll)))

(defn get-exercise-by-name [exercises name]
  (find-first #(= name (:name %)) exercises))

(defn current-round [{:keys [rest-seconds active-seconds]}]
  (let [round-length (+ rest-seconds active-seconds)]
    (int (Math/ceil (/ @seconds-since-start round-length)))))

(defn seconds-remaining-in-round [{:keys [rest-seconds active-seconds]}]
  (let [round-length (+ rest-seconds active-seconds)]
    (- round-length (mod @seconds-since-start round-length))))

(defn speak
  ([message] (speak message nil))
  ([message callback]
    (let [utterance (new js/SpeechSynthesisUtterance)
          voices (js/window.speechSynthesis.getVoices())]
      ;FIXME: Figure out how to change the voice
      ;(set! (.-voice utterance) (nth voices 48))
      (set! (.-lang utterance) "en-US")
      (set! (.-text utterance) message)
      (.addEventListener utterance "end" callback)
      (js/speechSynthesis.speak utterance))))

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
  [:div (workout :title)])

(defn round-component [workout]
  (let [round (current-round workout)]
    [:div
      "Round: " round]))

(defn current-exercise [{:keys [exercises] :as workout}]
  (let [round (current-round workout)]
    (get-exercise-by-name sample-exercises (nth exercises (- round 1)))))

(defn exercise-component [workout]
  [:div
    (:name (current-exercise workout))])

(defn callout-exercise [workout delay-remaining]
  (when (= delay-remaining 5) (speak (:callout (current-exercise workout)))))

(defn callout-dispatcher [{:keys [active-seconds]} time-remaining]
  (let [midround (/ active-seconds 2)]
    (cond
      (= time-remaining active-seconds) :start
      (= time-remaining midround) :middle
      (= time-remaining 15) :fifteen
      (= time-remaining 1) :end
      :else :none)))

(defmulti callout-times callout-dispatcher)
(defmethod callout-times :start [] (speak "start"))
(defmethod callout-times :middle [workout] (when (:switch (current-exercise workout)) (speak "switch")))
(defmethod callout-times :fifteen [] (speak "fifteen"))
(defmethod callout-times :end [] (speak "time"))
(defmethod callout-times :none [])

(defn countdown-component [{:keys [active-seconds] :as workout}]
  (let [round-remaining (seconds-remaining-in-round workout)
        delay-remaining (- round-remaining active-seconds)]
    (if (> delay-remaining 0)
      [:div
        (callout-exercise workout delay-remaining)
        "Round starting in " delay-remaining " seconds"]
      [:div
        (callout-times workout round-remaining)
        "Time remaining: " round-remaining " seconds"])))

(defn start-button-component []
  [:div
    [:input {:type "button" :value "Start" :on-click toggle-timer}]])

(defn exercise-lister [exercises]
  [:ol {:style {:text-align "left"}}
    (for [exercise exercises]
      ^{:key exercise} [:li exercise])])

(defn exercise-list-component [{:keys [exercises]}]
  [:div
    "Exercises:"
      [exercise-lister exercises]])

(defn workout-app [workout]
  [:div
    ;[timer-component]
    [title-component workout]
    [:br]
    [round-component workout]
    [exercise-component workout]
    [:br]
    [countdown-component workout]
    [start-button-component]
    [:br]
    [:br]
    [exercise-list-component workout]])
