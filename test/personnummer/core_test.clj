(ns personnummer.core-test
  (:require [clojure.test :refer [deftest is run-tests]]
            [personnummer.core :as SUT]))

(deftest luhn-sum
  (is (= 13 (SUT/luhn-sum [9 0 0 1 0 1 0 0 1]))))

(deftest luhn-sum
  (is (= 7 (SUT/luhn [9 0 0 1 0 1 0 0 1]))))

(run-tests)
