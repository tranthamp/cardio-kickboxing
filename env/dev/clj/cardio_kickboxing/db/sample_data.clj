(ns cardio-kickboxing.db.sample-data)

(def sample-exercises [{:name "Punching - Left" :targets [:upper] :difficulty 1 :callout "Punching, moving to the left" :switch false}
                       {:name "Punching - Right" :targets [:upper] :difficulty 1 :callout "Punching, moving to the right" :switch false}
                       {:name "Back Kicks" :targets [:lower] :difficulty 1 :callout "Because they work and I like them, back kicks" :switch false}
                       {:name "Fast Kicks" :targets [:lower] :difficulty 1 :callout "Fast kicks" :switch false}
                       {:name "Sliding Side Kicks" :targets [:lower] :difficulty 1 :callout "Sliding side kicks" :switch false}
                       {:name "Free Station - Left" :targets [:upper :lower] :difficulty 2 :callout "Left leg in front, free station" :switch true}
                       {:name "Free Station - Right" :targets [:upper :lower] :difficulty 2 :callout "Right leg in front, free station" :switch true}
                       {:name "Horse Stance Punching" :targets [:lower] :difficulty 1 :callout "Horse Stance Punching" :switch false}
                       {:name "Little Sister" :targets [:lower] :difficulty 3 :callout "Little sister" :switch false}
                       {:name "Front Leg Roundhouse Kicks" :targets [:lower] :difficulty 3 :callout "Front leg roundhouse kicks" :switch false}
                       {:name "In Her Honor" :targets [:upper :lower] :difficulty 3 :callout "In her honor" :switch false}])

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
