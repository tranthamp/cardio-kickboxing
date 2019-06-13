(ns cardio-kickboxing.test.handler
  (:require
    [clojure.test :refer :all]
    [ring.mock.request :refer :all]
    [cardio-kickboxing.handler :refer :all]
    [cardio-kickboxing.middleware.formats :as formats]
    [muuntaja.core :as m]
    [mount.core :as mount]))

(defn parse-json [body]
  (m/decode formats/instance "application/json" body))

(use-fixtures
  :once
  (fn [f]
    (mount/start #'cardio-kickboxing.config/env
                 #'cardio-kickboxing.handler/app)
    (f)))

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= 200 (:status response)))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= 404 (:status response))))))
