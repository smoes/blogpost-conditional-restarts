(ns blogpost-conditional-restarts.parser
  (:require [simple-restarts.core :refer :all])
  (:gen-class))

(def some-source ["valid line"
                  "valid line"
                  "invalid line"])


(defn valid? [line]
  (clojure.string/starts-with? line "valid"))


(defn do-parse [line]
  {:line line})


(defcondition invalid-line-error [line])


(defn parse-line [line]
  (if (valid? line)
    (do-parse line)
    (fire-condition (invalid-line-error line))))



(defn parse-lines []

  (doall ;; We need this because of lazyness, else nothing will be evaluated

   (for [line some-source]

     (restart-case

       (parse-line line)

       (restart :skip-line
         (fn [line] (println "Skipping line " line)))

       (restart :abort
         (fn [line] (throw (ex-info "Invalid line" {:line line}))))))))



(defn parse-lines-compiler []
  (handler-bind
    [invalid-line-error (fn [line]
                          (invoke-restart :abort line))]

    (parse-lines)))


(defn parse-lines-console []
  (handler-bind
    [invalid-line-error (fn [line]
                          (invoke-restart :skip-line line))]

    (parse-lines)))
