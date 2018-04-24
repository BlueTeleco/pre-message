(ns pre-message.core
  (:require [compojure.core :refer :all]
            [org.httpkit.server :refer [run-server]]
            [pre-message.controller :as c]
            [clojure.string :as s])
  (:gen-class))

; Transform a query to a map
(defn query->map [query]
  (reduce (fn [acc [k v]]
            (assoc acc (keyword k) v))
          {}
          (partition 2
                     (s/split query #"[=&]"))))

; Define the server router
(defroutes app
  (GET  "/"               []
       (c/global))

  (GET  "/sincronize/:id" [id :as {q :query-string}]
      (c/sinc-group id (:order (query->map q))))

  (POST "/add-user/:id"   [id phone]
      (c/user->group id phone))

  (POST "/send/:id"       [id text]
      (c/message->group id text))

  (PUT  "/new-user"       [uname phone pk]
      (c/new-user uname phone pk))

  (PUT  "/new-group"      [admin & others]
      (c/new-group admin others)))

; Main function. Starts the server in port 8080
(defn -main [& args]
  (println "HTTP server in port 8080.")
  (run-server app {:port 8080}))
