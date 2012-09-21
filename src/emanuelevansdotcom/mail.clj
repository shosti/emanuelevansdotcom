(ns emanuelevansdotcom.mail
  (:require (clojure            [string :as s])
            (clojure.java       [io :as io])
            (emanuelevansdotcom [cal :refer [events format-date]])
            (postal             [core :refer [send-message]])
            (clj-time           [core :refer [now after?]])))

(def from-email "emanuel.evans@gmail.com")
(def processed-fname "resources/mail/processed")

(defn format-event [event]
  (s/join "\n"
          [(format-date (:date event))
           (:summary event)
           (:link event)
           (s/join "\n" (:location event))
           (s/join "\n" (:description event))]))

(defn format-subject [event]
  (str "Upcoming Performance: "
       (:summary event)))

(defn message-body [event]
  (let [template (slurp "resources/mail/template")]
    (s/replace template
               "$event_details"
               (format-event event))))

(defn process-event [event maillist]
  (let [status
        (:error
         (send-message {:from from-email
                        :to ""
                        :bcc maillist
                        :subject (format-subject event)
                        :body [{:type "text/plain; charset=utf-8"
                                :content (message-body event)}]}))]
    (when (= status :SUCCESS)
      (with-open [processed (io/writer processed-fname
                                       :append true)]
        (spit processed (str (:id event) "\n"))))))

(defn process-events []
  (let [maillist (s/split-lines
                  (slurp "resources/mail/maillist"))
        processed (set (s/split-lines
                        (slurp processed-fname)))]
    (doseq [event (events)]
      (when (and (not (processed (:id event)))
                 (after? (:date event) (now)))
        (process-event event maillist)))))
