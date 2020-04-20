(ns blogpost-conditional-restarts.database
  (:require [simple-restarts.core :refer :all])
  (:gen-class))

;; Database effect example:

(defcondition database-effect [op params])

(defn database [op & params]
  (restart-case
    (fire-condition (database-effect op params))
    (restart :return-value identity)))


(def db-atom (atom {}))


(defn database-get [k]
  (get @db-atom k))


(defn database-put [k v]
  (swap! db-atom assoc k v))


(defn database-interpreter [op params]
  (case op
    :get (apply database-get params)
    :put (apply database-put params)))


(defn database-handler [op params]
  (invoke-restart :return-value (database-interpreter op params)))

(defn do-database-stuff []
  (database :put 1 "Kaan")
  (database :put 2 "Tim")
  (database :put 3 "Simon")
  [(database :get 2) (database :get 1) (database :get 3)])



(defn do-database-stuff-handled []
  (handler-bind
    [database-effect database-handler]
    (do-database-stuff)))

