(ns emanuelevansdotcom.core
  (:require (clojure.java [io :refer [file]])))

(defn file-exists? [fname]
  (let [f (file fname)]
    (.exists f)))

(def pages ["about" "concerts" "listen" "contact"])

(defn -main
  "I don't do a whole lot."
  [& args]
  ;;(spit "site/about.html" page)
  )
