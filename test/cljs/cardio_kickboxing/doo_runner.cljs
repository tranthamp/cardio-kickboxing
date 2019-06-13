(ns cardio-kickboxing.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [cardio-kickboxing.core-test]))

(doo-tests 'cardio-kickboxing.core-test)

