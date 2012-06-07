(ns emanuelevansdotcom.pages
  (:require (emanuelevansdotcom [layout :refer [site-page
                                                body-content]])))

(def about-page
  (site-page {:page-name "about"
              :body (body-content "about")
              :img-name "drawing.jpg"
              :img-caption "Artwork by Marisha Evans"}))