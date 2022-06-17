(ns example.core
  (:require [personnummer.core :as p]))

(defn -main []
  (let [pnr (p/personnummer "199001010017")]
    (println
     (format "The person with personal identity number %s is a %s of age %s"
             (p/format pnr) (p/gender pnr) (p/age pnr)))))
