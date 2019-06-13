(ns cardio-kickboxing.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [cardio-kickboxing.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[cardio-kickboxing started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[cardio-kickboxing has shut down successfully]=-"))
   :middleware wrap-dev})
