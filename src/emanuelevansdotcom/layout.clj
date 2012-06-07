(ns emanuelevansdotcom.layout
  (:use (hiccup page element)))

(def page
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:title "Emanuel Evans, Cellist"]
    [:meta {:name "viewport", :content "width=device-width, initial-scale=1.0"}]
    "<!--[if lte IE 9]>
<link rel=\"stylesheet\" href=\"css/ie.css\" type=\"text/css\" media=\"screen\" />
<![endif]-->"
    (include-css "css/style.css")
    (include-js "js/css3-mediaqueries.js")]
   [:body
    [:div.container
     [:div.row
      [:div.sixcol.info
       [:div
        [:h1 "Emanuel Evans"]
        [:h2 "Cellist"]]
       [:div.navbar
        [:span.active "About"] " | "
        (link-to "#" "Concerts") " | "
        (link-to "#" "Listen") " | "
        (link-to "#" "Contact")]
       [:p
        "Lorem ipsum dolor sit amet, consectetur adipisicing elit,\n
        sed do eiusmod tempor incididunt ut labore et dolore magna\n
        aliqua. Ut enim ad minim veniam, quis nostrud exercitation\n
        ullamco laboris nisi ut aliquip ex ea commodo\n
        consequat. Duis aute irure dolor in reprehenderit in\n
        voluptate velit esse cillum dolore eu fugiat nulla\n
        pariatur. Excepteur sint occaecat cupidatat non proident,\n
        sunt in culpa qui officia deserunt mollit anim id est\n
        laborum."]]
      [:div.sixcol.last.picture
       (image "images/drawing.jpg" "Drawing of Emanuel Evans by Marisha Evans")
       [:div.caption "Artwork by Marisha Evans"]]]]]))