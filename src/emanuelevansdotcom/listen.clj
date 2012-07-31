(ns emanuelevansdotcom.listen
  (:require (clojure      [string :as s])
            (clojure.java [io :as io])
            (hiccup       [element :refer [link-to]])))

(def audio-re #"\d+_(.+).aiff")

(defn format-audio [fname]
  (let [[_ t] (re-find audio-re fname)
        audio-title (s/replace t #"_" " ")
        audio-path (str "audio/" fname)]
    [:p
     [:strong audio-title]
     [:br]
     [:audio {:controls "controls"}
      [:source {:src (s/replace audio-path #"aiff" "mp3")
                :type "audio/mpeg"}]
      [:source {:src (s/replace audio-path #"aiff" "ogg")
                :type "audio/ogg"}]
      (link-to audio-path "Listen now")]]))

(defn list-audio-files [dir]
  (->> dir
       (io/file)
       (file-seq)
       (map str)
       (map #(s/split % #"/"))
       (flatten)
       (filter #(re-matches audio-re %))))

(defn listen-content [_]
  (map format-audio (list-audio-files "resources/audio")))
