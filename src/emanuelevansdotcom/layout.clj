(ns emanuelevansdotcom.layout
  (:require (emanuelevansdotcom [pages :refer [page-names]])
            (clojure [string  :as s])
            (environ [core :refer [env]])
            (hiccup  [core    :refer [html]]
                     [element :refer [link-to image]]
                     [page    :refer [html5 include-css include-js]]
                     [form :as f])))

(def compatibility
  "<!--[if lte IE 9]> <link rel=\"stylesheet\" href=\"css/ie.css\"
type=\"text/css\" media=\"screen\" /> <![endif]-->")

(def analytics
  "<script type=\"text/javascript\">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-33865685-1']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>")

(defn navbar [active-page]
  [:div.navbar
   (interpose " | "
              (map (fn [page]
                     (if (= page active-page)
                       [:span.active (s/capitalize page)]
                       (link-to (str page ".html")
                                (s/capitalize page))))
                   page-names))])

(defn subscribe-form [active-page]
  [:div.subscribe
   [:br] [:br]
   "Subscribe to my mailing list" [:br]
   [:form#subscribe {:method "POST"
                     :action (str (env :api-url) "/subscribe")}
    [:input {:type "email"
             :name "email"
             :placeholder "Email"}]
    [:input {:type "hidden"
             :name "redirect"
             :value (str (env :domain) "/" active-page ".html")}]
    (f/submit-button "Subscribe")]])

(defn site-page [{:keys [page-name content img-name img-caption]}]
  (html5 {:lang "en-us"}
   [:head
    [:meta {:charset "utf-8"}]
    [:title "Emanuel Evans, Cellist - " (s/capitalize page-name)]
    [:meta {:name "viewport",
            :content "width=device-width, initial-scale=1.0"}]
    [:link {:href "favicon.ico",
            :rel "shortcut icon",
            :type "image/x-icon"}]
    (include-css "css/style.css")
    compatibility
    (include-js "http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js")
    (include-js "js/scripts.js")
    analytics]
   [:body
    [:div.container
     [:div.row
      [:div.sixcol.content
       [:div
        [:h1 "Emanuel Evans"]
        [:h2 "Cellist"]]
       (navbar page-name)
       content]
      [:div.sixcol.last.picture
       (image (str "images/" img-name) "")
       [:div.caption img-caption]
       (subscribe-form page-name)]]]]))
