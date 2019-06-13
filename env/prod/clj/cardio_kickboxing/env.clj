(ns cardio-kickboxing.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[cardio-kickboxing started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[cardio-kickboxing has shut down successfully]=-"))
   :middleware identity})
