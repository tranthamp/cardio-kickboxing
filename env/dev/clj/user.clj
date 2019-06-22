(ns user
  "Userspace functions you can run by default in your local REPL."
  (:require
    [cardio-kickboxing.config :refer [env]]
    [clojure.spec.alpha :as s]
    [expound.alpha :as expound]
    [mount.core :as mount]
    [cardio-kickboxing.figwheel :refer [start-fw stop-fw cljs]]
    [cardio-kickboxing.core :refer [start-app]]
    [cardio-kickboxing.db.core :as db]
    [conman.core :as conman]
    [luminus-migrations.core :as migrations]
    [cardio-kickboxing.db.sample-data :as sample-data]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(add-tap (bound-fn* clojure.pprint/pprint))

(defn start 
  "Starts application.
  You'll usually want to run this on startup."
  []
  (mount/start-without #'cardio-kickboxing.core/repl-server))

(defn stop 
  "Stops application."
  []
  (mount/stop-except #'cardio-kickboxing.core/repl-server))

(defn restart 
  "Restarts application."
  []
  (stop)
  (start))

(defn restart-db 
  "Restarts database."
  []
  (mount/stop #'cardio-kickboxing.db.core/*db*)
  (mount/start #'cardio-kickboxing.db.core/*db*)
  (binding [*ns* 'cardio-kickboxing.db.core]
    (conman/bind-connection cardio-kickboxing.db.core/*db* "sql/queries.sql")))

(defn reset-db 
  "Resets database."
  []
  (migrations/migrate ["reset"] (select-keys env [:database-url])))

(defn migrate 
  "Migrates database up for all outstanding migrations."
  []
  (migrations/migrate ["migrate"] (select-keys env [:database-url])))

(defn rollback 
  "Rollback latest database migration."
  []
  (migrations/migrate ["rollback"] (select-keys env [:database-url])))

(defn create-migration 
  "Create a new up and down migration file with a generated timestamp and `name`."
  [name]
  (migrations/create name (select-keys env [:database-url])))

(defn seed-db
  "Seeds the database with example data"
  []
  (do
    (reset-db)
    (map db/create-exercise! sample-data/sample-exercises)))





