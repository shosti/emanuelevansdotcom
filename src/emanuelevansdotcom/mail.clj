(ns emanuelevansdotcom.mail
  (:require (clojure            [string :as s])
            (clojure.java       [io :as io])
            (emanuelevansdotcom [cal :refer [events format-date]])
            (postal             [core :refer [send-message]])
            (cheshire [core :as json])
            (clj-http [client :as client])
            (clj-time           [core :refer [now after?]])
            (environ [core :refer [env]])))

(def from-email "emanuel.evans@gmail.com")
(def processed-fname "resources/mail/processed")
(def signature-fname "resources/mail/signature")

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
    (str (s/replace template
                    "$event_details"
                    (format-event event))
         (slurp signature-fname))))

(defn process-message [body subject id]
  (println "Sending message with id " id)
  (let [response
        (json/parse-string
         (client/post (str (env :api-url-secure)
                           "/send-maillist-message")
                      {:basic-auth [(env :api-user) (env :api-key)]
                       :form-params {:body body
                                     :subject subject}}))]
    (when (= (response "status") "success")
      (with-open [processed (io/writer processed-fname
                                       :append true)]
        (spit processed (str id "\n"))))))

(defn process-event [event]
  (process-message (message-body event)
                   (format-subject event)
                   (:id event)))

(defn process-events [processed]
  (doseq [event (events)]
    (when (and (not (processed (:id event)))
               (after? (:date event) (now)))
      (process-event event))))

(defn process-messages [processed]
  (doseq [f (file-seq (io/file "resources/mail/messages"))]
    (let [fname (.getName f)]
      (when (and (not (contains? #{"messages" ".DS_Store"} fname))
                 (not (processed fname)))
        (process-message (str (slurp f)
                              (slurp signature-fname))
                         (s/replace ((s/split fname #"_" 2) 1)
                                    #"_" " ")
                         fname)))))

(defn process-mail []
  (let [processed (set (s/split-lines
                        (slurp processed-fname)))]
    (process-events processed)
    (process-messages processed)))

(defn -main [& args]
  (println "Sending mail...")
  (process-mail))