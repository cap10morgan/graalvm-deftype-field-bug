(ns gdfb.core
  (:require [clojure.core.async :refer [go]])
  (:gen-class))


(set! *warn-on-reflection* true)


(deftype Thingy [a b c])


(defn -main [& _]
  (let [^Thingy thing (->Thingy "a" "b" "c")]
    (println "Outside the go block:" (.-a thing)) ; this works on regular JVM & graalvm native-image
    (go
      (let [^Thingy other (->Thingy "1" "2" "3")]
        (println "Inside the go block:" (.-a other))))) ; this works on regular JVM but blows up on graalvm native-image
  (Thread/sleep 2000))
