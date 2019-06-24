(ns cardio-kickboxing.routes.home
  (:require
    [cardio-kickboxing.layout :as layout]
    [cardio-kickboxing.gen-workout :as gen]
    [clojure.java.io :as io]
    [cardio-kickboxing.middleware :as middleware]
    [ring.util.http-response :as response]))

(defn home-page [request]
  (layout/render request "home.html"))

(defn gen-workout [request]
  (response/ok (gen/gen-workout)))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/"            {:get home-page}]
   ["/gen-workout" {:get gen-workout}]])
