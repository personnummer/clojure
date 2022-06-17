(ns personnummer.core-test
  (:require [clojure.test :refer [deftest are run-tests]]
            [java-time :as t]
            [personnummer.core :as SUT]))

(deftest invalid-date
  (are
   [input]
   (= nil (:date (SUT/personnummer input)))
    "invalid-date"
    "19900140-0001"
    "201702290001"))

(deftest invalid-digits
  (are
   [input]
   (and
    (not= nil (:date (SUT/personnummer input)))
    (not (SUT/valid (SUT/personnummer input))))
    "19900101-0018"
    "20160229-1111"
    "6403273814"
    "20150916-0006"))

(deftest valid-personnummer
  (are
   [input]
   (= true (SUT/valid (SUT/personnummer input)))
    "19900101-0017"
    "196408233234"
    "000101-0107"
    "510818-9167"
    "19130401+2931"))

(deftest gender
  (are
   [input is_female]
   (= is_female (SUT/is-female (SUT/personnummer input)))
    "19090903-6600" true
    "19900101-0017" false
    "800101-3294" false
    "000903-6609" true
    "800101+3294" false))

(deftest coordination
  (are
   [input expected]
   (= expected (:coordination (SUT/personnummer input)))
    "800161-3294" true
    "800101-3294" false
    "640327-3813" false))

(def twenty-year-old
  (let [now t/local-date
        twenty-years-ago (- (t/as (now) :year) 20)]
    (format "%s0101-0001" twenty-years-ago)))

(def twenty-year-old-today
  (let [now t/local-date
        twenty-years-ago (- (t/as (now) :year) 20)
        this-month (t/as (now) :month-of-year)
        this-day (t/as (now) :day-of-month)]
    (format "%s%02d%02d-0001" twenty-years-ago this-month this-day)))

(def twenty-year-old-tomorrow
  (let [now t/local-date
        twenty-years-ago (- (t/as (now) :year) 20)
        this-month (t/as (now) :month-of-year)
        this-day (t/as (now) :day-of-month)]
    (format "%s%02d%02d-0001" twenty-years-ago this-month (+ this-day 1))))

(deftest age
  (are
   [input age]
   (= age (SUT/age (SUT/personnummer input)))
    twenty-year-old 20
    twenty-year-old-today 20
    twenty-year-old-tomorrow 19))

(deftest format-pnr
  (are
   [input long expected]
   (= expected (SUT/format (SUT/personnummer input) long))
    "9001010017" false "900101-0017"
    "9001010017" true "19900101-0017"
    "900101-0017" false "900101-0017"
    "900101-0017" true "19900101-0017"
    "19900101-0017" false "900101-0017"
    "19900101-0017" true "19900101-0017"
    "19900101+0017" false "900101+0017"
    "19900101+0017" true "19900101+0017"))

(run-tests)
