(ns personnummer.core
  (:require [clojure.string :refer (join split)]
            [clj-time.core :as t]))

(def pnr-re
  #"^([0-9]{2})?([0-9]{2})([0-9]{2})([0-9]{2})([-+])?([0-9]{3})([0-9])?$")

(defrecord Personnummer
           [date serial control separator coordination])

(defn personnummer
  "Create a new instance of Personnummer."
  [input]
  (let [date (re-matches pnr-re input)
        century (read-string (nth date 1 "20"))
        year (read-string (nth date 2 "0"))
        month (read-string (nth date 3 "0"))
        day (read-string (nth date 4 "0"))
        divider (nth date 5 "-")
        serial (read-string (nth date 6 "000"))
        control (read-string (nth date 7 "0"))]
    (->Personnummer
     (t/date-time (+ (* century 100) year) month day 0 0 0 0)
     serial
     control
     divider
     (> day 60))))

(defn luhn-sum [digits]
  (apply
   +
   (map read-string
        (split
         (join ""
               (map * digits
                    (cycle [2 1])))
         #""))))

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
   (= (luhn-string "900101001") (:control pnr))))
