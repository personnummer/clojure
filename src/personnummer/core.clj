(ns personnummer.core
  (:require [clojure.string :refer (join split)]))

(defrecord Personnummer
           [date serial control separator coordination])

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
