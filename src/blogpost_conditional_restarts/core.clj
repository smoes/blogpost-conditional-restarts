(ns blogpost-conditional-restarts.core
  (:require [simple-restarts.core :refer :all]
            [blogpost-conditional-restarts.database :as database]
            [blogpost-conditional-restarts.parser :as parser])
  (:gen-class))



(defn -main
  "I don't do a whole lot ... yet."
  [& args]

  (println "database example: ")
  (println (database/do-database-stuff-handled))

  (println "console parser example: ")
  (parser/parse-lines-console)

  (println "compiler parser example: ")
  (parser/parse-lines-compiler))
