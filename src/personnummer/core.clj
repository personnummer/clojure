(ns personnummer.core
  (:require [clojure.string :refer (join split)]
            [java-time :as t]
            [personnummer.gender :as gender]))

(defrecord Personnummer
           [date serial control separator coordination])

(def pnr-re
  #"^([0-9]{2})?([0-9]{2})([0-9]{2})([0-9]{2})([-+])?([0-9]{3})([0-9])?$")

(defn personnummer
  "Create a new instance of Personnummer."
  [input]
  (if-some [[_ century year month day divider serial control]
            (re-matches pnr-re input)]
    (->Personnummer
     (t/local-date (+ (* (Integer/parseInt (or century "19")) 100) (Integer/parseInt year))
                   (Integer/parseInt month)
                   (Integer/parseInt day))
     (read-string serial)
     (read-string control)
     divider
     (> (Integer/parseInt day) 60))
    (Personnummer. nil 0 0 "" false)))

(defn luhn-sum [digits]
  (apply
   +
   (map read-string
        (split
         (join ""
               (map * digits
                    (cycle [2 1])))
         #""))))

(defn format-for-luhn [pnr]
  (format "%s%03d"
          (t/format "yyMMdd" (:date pnr))
          (:serial pnr)))

(defn luhn [digits]
  (- 10
     (mod
      (luhn-sum digits) 10)))

(defn luhn-string [string]
  (luhn (map read-string (split string #""))))

(defmulti valid class)
(defmethod valid personnummer.core.Personnummer [pnr]
  (and
   (not= nil? (:date pnr))
   (> (:serial pnr) 0)
   (= (luhn-string (format-for-luhn pnr)) (:control pnr))))
(defmethod valid java.lang.String [pnr]
  (valid (personnummer pnr)))

(defn gender [pnr]
  (let [c (mod (mod (:serial pnr) 10) 2)]
    (cond
      (= 0 c) gender/female
      :else gender/male)))

(defn is-female [pnr]
  (= (gender pnr) gender/female))

(defn is-male [pnr]
  (not (is-female pnr)))

(defn age [pnr]
  (let [now t/local-date
        y (t/as (:date pnr) :year) y-now (t/as (now) :year)
        m (t/as (:date pnr) :month-of-year) m-now (t/as (now) :month-of-year)
        d (t/as (:date pnr) :day-of-month) d-now (t/as (now) :day-of-month)]
    (cond
      (> m-now m) (- y-now y)
      (and
       (= m-now m)
       (>= d-now d))
      (- y-now y)
      :else
      (- (- y-now y) 1))))
