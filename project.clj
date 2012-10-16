(defproject emanuelevansdotcom "0.1.0-SNAPSHOT"
  :description "Simple site generator for emanuelevans.com"
  :url "http://www.emanuelevans.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main emanuelevansdotcom.core
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [hiccup "1.0.0"]
                 [clodown "1.0.2"]
                 [clj-time "0.4.2"]
                 [clj-http "0.4.3"]
                 [com.draines/postal "1.8.0"]
                 [environ "0.3.0"]]
  :profiles {:dev {:env {:api-url "http://api.emanuelevans.com"
                         :api-url-secure
                         "https://emanuelevans-api.herokuapp.com"
                         :domain "http://www.emanuelevans.com"}}})
