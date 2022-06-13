(defproject personnummer/personnummer "0.1.0"
  :description "Validate Swedish personal identity numbers"
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [clj-time "0.15.2"]]
  :url "https://github.com/bombsimon/clojure-personnummer"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :sign-releases false}]]
  :min-lein-version "2.0.0"
  :repl-options {:init-ns personnummer.core})
