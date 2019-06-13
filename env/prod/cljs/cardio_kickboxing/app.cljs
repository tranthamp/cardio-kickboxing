(ns cardio-kickboxing.app
  (:require [cardio-kickboxing.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
