(ns gdfb.core
  (:require [clojure.core.async :refer [go]])
  (:gen-class))


(deftype Thingy [a b c])


(defn -main [& _]
  (let [thing (->Thingy "a" "b" "c")]
    (println "Outside the go block:" (.-a thing)) ; this works on regular JVM & graalvm native-image
    (go
      (println "Inside the go block:" (.-a thing)))) ; this works on regular JVM but blows up on graalvm native-image
  (Thread/sleep 2000))
