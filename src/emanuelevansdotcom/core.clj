(ns emanuelevansdotcom.core
  (:use emanuelevansdotcom.layout))

(defn -main
  "I don't do a whole lot."
  [& args]
  (spit "site/about.html" page))
