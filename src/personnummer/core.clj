(ns personnummer.core
  (:require [clojure.string :refer (join split)]
            [clj-time.core :as t]
            [clj-time.format :as tf]))

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
     (t/date-time (+ (* (read-string century) 100) (read-string year)) (read-string month) (read-string day) 0 0 0 0)
     (read-string serial)
     (read-string control)
     divider
     (> (read-string day) 60))
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
          (tf/unparse (tf/formatter "yyMMdd") (:date pnr))
          (:serial pnr)))

(defn luhn [digits]
  (- 10
     (mod
      (luhn-sum digits) 10)))

(defn luhn-string [string]
  (luhn (map read-string (split string #""))))

(defn valid [pnr]
  (and
   (not= nil? (:date pnr))
   (> (:serial pnr) 0)
   (= (luhn-string (format-for-luhn pnr)) (:control pnr))))
