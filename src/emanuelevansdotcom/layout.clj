(ns emanuelevansdotcom.layout
  (:require (emanuelevansdotcom [pages :refer [page-names]])
            (clojure [string  :as s])
            (hiccup  [core    :refer [html]]
                     [element :refer [link-to image]]
                     [page    :refer [html5 include-css include-js]])))

(def compatibility
  "<!--[if lte IE 9]> <link rel=\"stylesheet\" href=\"css/ie.css\"
type=\"text/css\" media=\"screen\" /> <![endif]-->")

(defn navbar [active-page]
  [:div.navbar
   (interpose " | "
              (map (fn [page]
                     (if (= page active-page)
                       [:span.active (s/capitalize page)]
                       (link-to (str page ".html")
                                (s/capitalize page))))
                   page-names))])

(defn site-page [{:keys [page-name content img-name img-caption]}]
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:title "Emanuel Evans, Cellist - " (s/capitalize page-name)]
    [:meta {:name "viewport",
            :content "width=device-width, initial-scale=1.0"}]
    compatibility
    (include-css "css/style.css")
    (include-js "js/css3-mediaqueries.js")]
   [:body
    [:div.container
     [:div.row
      [:div.sixcol.info
       [:div
        [:h1 "Emanuel Evans"]
        [:h2 "Cellist"]]
       (navbar page-name)
       content]
      [:div.sixcol.last.picture
       (image (str "images/" img-name) "")
       [:div.caption img-caption]]]]]))
