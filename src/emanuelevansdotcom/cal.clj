 (ns emanuelevansdotcom.cal
  (:refer-clojure :exclude [extend])
  (:require (clojure  [string :as s])
            (clj-http [client :as client])
            (cheshire [core :as json])
            (clj-time [core :refer :all]
                      [format :refer [parse]])
            (hiccup   [element :refer [link-to]]))
  (:import java.net.URLEncoder))

"# Get and process json data"

(defn get-gcal-json
  "Fetch calendar data from Google"
  [cal-id api-key]
  (try
    (:body
     (client/get
      (str "https://www.googleapis.com/calendar/v3/calendars/"
           cal-id
           "/events?key="
           api-key)))
    (catch Exception e
      (prn (str "Could not fetch json: " e)))))

(defn process-description
  [description]
  (let [lines (s/split-lines description)
        link (first lines)
        desc (filter #(not (= % "")) (rest lines))]
    {:link link
     :description desc}))

(defn process-location
  [location]
  {:location-link (str "http://maps.google.com/maps?q="
                       (URLEncoder/encode location))
   :location (map s/trim (s/split location #";"))})

(defn process-gcal-event
  [event]
  (-> event
     (select-keys [:summary :id])
     (merge {:date (to-time-zone
                    (parse (:dateTime (:start event)))
                    (time-zone-for-id (:timeZone
                                         (:start event))))})
     (merge (process-description (:description event)))
     (merge (process-location (:location event)))))

(defn gcal-json->event-list [json]
  (try
    (map process-gcal-event
         (:items (json/parse-string json true)))
    (catch Exception e
      (prn (str "Could not parse json: " e)))))

"# IO and config"

(def cal-json-file "resources/pages/cal.json")

(defn events []
  (gcal-json->event-list (slurp cal-json-file)))

(defn cal-id []
  (s/trim (slurp "resources/private/cal-id")))

(defn api-key []
  (s/trim (slurp "resources/private/api-key")))

(defn fetch-cal []
  (spit cal-json-file
        (get-gcal-json (cal-id) (api-key))))

(defn -main [& args]
  (println "Fetching calendar...")
  (fetch-cal))

"# Generate html from event list"

(defn format-date [d]
  (let [days [""
              "Monday"
              "Tuesday"
              "Wednesday"
              "Thursday"
              "Friday"
              "Saturday"
              "Sunday"]
        months [""
                "January"
                "February"
                "March"
                "April"
                "May"
                "June"
                "July"
                "August"
                "September"
                "October"
                "November"
                "December"]]
    (str (days (day-of-week d))
         ", "
         (months (month d))
         " "
         (day d)
         ", "
         (year d)
         ", "
         (.toString d "h:mm")
         (if (< (hour d) 12)
           " AM"
           " PM"))))

(defn sort-events [events]
  (let [now (now)
        split-events
        (->> events
             (sort-by :date)
             (split-with #(before? (:date %) now)))]
    {:past (reverse (first split-events))
     :upcoming (fnext split-events)}))

(defn format-event [event]
  [:p
   (interpose [:br]
              [[:strong (format-date (:date event))]
               [:strong (link-to (:link event)
                                 (:summary event))]
               (link-to (:location-link event)
                        (interpose [:br] (:location event)))
               (interpose [:br] (:description event))])])

(defn cal-content [_]
  (let [events (sort-events (events))
        ical-address
        (str "https://www.google.com/calendar/ical/"
             (URLEncoder/encode (cal-id))
             "/public/basic.ics")]
    (concat
     (if-let [upcoming
              (seq (map format-event
                        (:upcoming events)))]
       [[:h3 "Upcoming"]
        upcoming])
     (if-let [past
              (seq (map format-event
                        (take 5 (:past events))))]
       [[:h3 "Past"]
        past])
     [[:p.caption
       "To subscribe to my mailing list, send an email to "
       (let [subscription-address "subscribe@emanuelevans.com"]
         (link-to (str "mailto:" subscription-address)
                  subscription-address))
       ".  You can also subscribe with "
       (link-to
        (str "http://www.google.com/calendar/render?cid="
             ical-address)
        "Google Calendar")
       " or "
       (link-to ical-address
                "Apple Calendar")
       "."]])))