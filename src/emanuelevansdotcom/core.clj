(ns emanuelevansdotcom.core
  (:require (emanuelevansdotcom [pages :refer [pages page-names]]
                                [layout :refer [site-page]])
            (clojure [string :as s])
            (clojure.java [io :refer [file]])
            (clodown [core :refer [md]])))

(defn file-exists? [fname]
  (let [f (file fname)]
    (.exists f)))

(defn md-content [page-name]
  (let [md-fname (str "resources/pages/" page-name ".md")]
    (when (file-exists? md-fname)
      (md (slurp md-fname)))))

(defn page-html [[page-id page]]
  (let [page-name (name page-id)
        content-fn (:content-fn page md-content)]
    (site-page (into page
                     {:page-name page-name
                      :content (content-fn page-name)}))))

(defn generate-sitemap []
  (spit "_site/sitemap.txt"
        (s/join "\n"
                (map #(str "http://www.emanuelevans.com/" % ".html")
                     page-names))))

(def html-pages
  (map vector page-names
       (map page-html pages)))

(defn generate-html []
  (doseq [html-page html-pages]
    (spit (str "_site/" (first html-page) ".html")
          (html-page 1))))

(defn -main
  "Generate html and sitemap."
  [& args]
  (generate-html)
  (generate-sitemap))
