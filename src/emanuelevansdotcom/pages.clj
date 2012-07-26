(ns emanuelevansdotcom.pages
  ;; (:require (emanuelevansdotcom [cal :refer format-cal]))
  )

(def pages
  (array-map
   :about {    :img-name "drawing.jpg"
               :img-caption "Artwork by Marisha Evans"}
   :concerts {; :content-fn format-cal
               :img-name "drawing.jpg"
               :img-caption "Artwork by Marisha Evans"}
   :teaching { :img-name "drawing.jpg"
               :img-caption "Artwork by Marisha Evans"}
   :listen {   :img-name "drawing.jpg"
               :img-caption "Artwork by Marisha Evans"}
   :contact {  :img-name "drawing.jpg"
               :img-caption "Artwork by Marisha Evans"}))

(def page-names
  (map name (keys pages)))
