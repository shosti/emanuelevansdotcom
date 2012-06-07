(ns emanuelevansdotcom.pages)

(def pages
  (array-map
   :about {    :img-name "drawing.jpg"
               :img-caption "Artwork by Marisha Evans"}
   :concerts { :content-fn (fn [_] "I am special")
               :img-name "drawing.jpg"
               :img-caption "Artwork by Marisha Evans"}
   :listen {   :img-name "drawing.jpg"
               :img-caption "Artwork by Marisha Evans"}
   :contact {  :img-name "drawing.jpg"
               :img-caption "Artwork by Marisha Evans"}))

(def page-names
  (map name (keys pages)))