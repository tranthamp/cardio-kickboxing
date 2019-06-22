(ns cardio-kickboxing.routes.home
  (:require
    [cardio-kickboxing.layout :as layout]
    [cardio-kickboxing.db.core :as db]
    [clojure.java.io :as io]
    [cardio-kickboxing.middleware :as middleware]
    [ring.util.http-response :as response]))

(defn home-page [request]
  (layout/render request "home.html"))

(defn get-exercises [request]
  nil)

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   ["/exercises" {:get get-exercises}]])
