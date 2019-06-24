(ns cardio-kickboxing.test.db.core
  (:require
    [cardio-kickboxing.db.core :refer [*db*] :as db]
    [luminus-migrations.core :as migrations]
    [clojure.test :refer :all]
    [clojure.java.jdbc :as jdbc]
    [cardio-kickboxing.config :refer [env]]
    [mount.core :as mount]))

(use-fixtures
  :once
  (fn [f]
    (mount/start
      #'cardio-kickboxing.config/env
      #'cardio-kickboxing.db.core/*db*)
    (migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (f)))

(deftest test-exercises
  (jdbc/with-db-transaction [t-conn *db*]
    (jdbc/db-set-rollback-only! t-conn)
    (is (= 1 (db/create-exercise!
               t-conn
               {:id         "1"
                :name       "Test"
                :callout    "Test Callout"
                :switch     false})))
    (is (= {:id         "1"
            :name       "Test"
            :callout    "Test Callout"
            :switch     false}
           (db/get-exercise t-conn {:id "1"})))))
