(ns emanuelevansdotcom.pages
  (:require (emanuelevansdotcom [cal :refer [cal-content]]
                                [listen :refer [listen-content]])))

(def pages
  (array-map
   :about {    :img-name "photo.jpg"
               :img-caption "Photo by Allegra Chapman"}
   :concerts { :content-fn cal-content
               :img-name "drawing.jpg"
               :img-caption "Artwork by Marisha Evans"}
   :teaching { :img-name "photo.jpg"
               :img-caption "Photo by Allegra Chapman"}
   :listen {   :content-fn listen-content
               :img-name "drawing.jpg"
               :img-caption "Artwork by Marisha Evans"}
   :contact {  :img-name "drawing.jpg"
               :img-caption "Artwork by Marisha Evans"}))

(def page-names
  (map name (keys pages)))
