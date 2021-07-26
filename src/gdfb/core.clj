(ns gdfb.core
  (:require [clojure.core.async :refer [go]])
  (:gen-class))


(deftype Thingy [a b c])


(defn -main [& _]
  (let [thing (->Thingy "a" "b" "c")]
    (println "Outside the go block:" (.-a thing))
    (go
      (println "Inside the go block:" (.-a thing))))
  (Thread/sleep 2000))
