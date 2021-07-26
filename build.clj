(ns build
  (:require [clojure.tools.build.api :as b]
            [clojure.string :as str]
            [clojure.java.io :as io])
  (:import (java.io File)))

(def main-ns 'gdfb.core)
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))
(def native-image-name "gdfb")
(def jar-file-name (str "target/" native-image-name ".jar"))

(defn clean [_]
  (b/delete {:path "gdfb"})
  (b/delete {:path class-dir}))

(defn- native-image-bin
  "Stolen from clj.native-image"
  []
  (let [graal-paths [(str (System/getenv "GRAALVM_HOME") "/bin")
                     (System/getenv "GRAALVM_HOME")]
        paths (lazy-cat graal-paths (str/split (System/getenv "PATH") (re-pattern (File/pathSeparator))))
        filename "native-image"]
    (first
      (for [path (distinct paths)
            :let [file (io/file path filename)]
            :when (.exists file)]
        (.getAbsolutePath file)))))

(defn native-image [_]
  (b/write-pom {:class-dir class-dir
                :lib       'com.fluree/gdfb.core
                :basis     basis
                :version   "0.1.0"
                :src-dirs  ["src"]})                        ; shouldn't this come from the basis?
  (b/compile-clj {:basis        basis
                  :src-dirs     ["src"]
                  :class-dir    class-dir
                  :compile-opts {:direct-linking true}})
  (b/uber {:class-dir class-dir
           :uber-file jar-file-name
           :basis     basis
           :main      main-ns})
  (b/process {:command-args [(native-image-bin)
                             "-jar" jar-file-name
                             native-image-name
                             "--initialize-at-build-time"
                             "--no-fallback"]}))
