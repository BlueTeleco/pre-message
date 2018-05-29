(ns pre-message.core
  (:require [compojure.core         :refer :all]
            [org.httpkit.server     :refer [run-server]]
            [pre-message.controller :as     c]
            [ring.middleware.params :refer [wrap-params]]
            [clojure.string         :as     s])
  (:gen-class))

; Define the server router
(defroutes router
  (GET  "/"               []
       (c/global))

  (GET  "/sincronize/:chat" [chat order]
      (c/sinc-group chat order))

  (POST "/add-user/:chat"   [chat phone]
      (c/user->group chat phone))

  (POST "/send/:chat"       [text group sender]
      (c/message->group text group sender))

  (PUT  "/new-user"       [uname phone pk]
      (c/new-user uname phone pk))

  (PUT  "/new-group"      [admin-ph gname]
      (c/new-group admin-ph gname)))

; Wraps the router in the middleware that allows parameter destructuring
(def app (wrap-params router))

; Main function. Starts the server in port 8080
(defn -main [& args]
  (println "HTTP server in port 8080.")
  (run-server app {:port 8080}))
