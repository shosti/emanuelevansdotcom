(ns emanuelevansdotcom.cal
  (:require (clojure  [string :as s])
            (clj-http [client :as client])
            (cheshire [core :as json])
            (clj-time [core :as t]
                      [format :as time-format])
            (hiccup   [element :refer [link-to]])))

"# Get and process json data"

(defn get-gcal-json
  "Fetch calendar data from google"
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

(defn process-json-date [d]
  (time-format/parse (first (vals d))))

(defn process-gcal-event
  [event]
  (-> event
     (select-keys [:htmlLink :location :summary :description])
     (merge {:start (process-json-date (:start event))
             :end (process-json-date (:end event))})))

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

(defn fetch-cal []
    (let [api-key (s/trim (slurp "resources/private/api-key"))
          cal-id (s/trim (slurp "resources/private/cal-id"))]
      (spit cal-json-file
            (get-gcal-json cal-id api-key))))

(defn -main [& args]
  (fetch-cal))

"# Generate html from event list"

(defn sort-events [events]
  (let [now (t/now)
        split-events
        (->> events
             (sort-by :start)
             (split-with #(t/before? (:start %) now)))]
    {:past (reverse (first split-events))
     :upcoming (fnext split-events)}))

(defn format-event [event]
  [:p
   [:strong {:start event}
    (if-let [end {:end event}] (str "-" end))] [:br]
   ])

(defn format-cal [_]
  (let [events (events)]
    nil))