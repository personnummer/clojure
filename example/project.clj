(defproject example "0.1.0-SNAPSHOT"
  :description "This is an example application using perosnnummer"
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [personnummer "0.1.0"]]
  :repl-options {:init-ns example.core}
  :main example.core)
