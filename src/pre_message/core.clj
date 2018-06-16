(ns pre-message.core
  (:require [compojure.core         :refer :all]
            [org.httpkit.server     :refer [run-server]]
            [pre-message.controller :as     c]
            [ring.middleware.params :refer [wrap-params]]
            [clojure.string         :as     s])
  (:gen-class))

; Define the server router
(defroutes router
  (GET  "/"                              []
       (c/global))

  (GET  "/chats/:phone"                  [phone]
      (c/chats phone))

  (GET  "/public-key/:phone"             [phone]
      (c/pubkey-user phone))

  (GET  "/pubkey-admin/:chat"            [chat]
      (c/pubkey-admin chat))

  (GET  "/rencryption-key/:chat/:phone"  [phone chat]
      (c/rekey-users phone chat))

  (GET  "/sincronize/:chat"              [chat order]
      (c/sinc-group chat order))

  (POST "/add-user/:chat"                [chat phone rk]
      (c/user->group chat phone rk))

  (POST "/send/:chat"                    [text chat phone]
      (c/message->group text chat phone))

  (PUT  "/new-user"                      [uname phone pk]
      (c/new-user uname phone pk))

  (PUT  "/new-group"                     [admin-ph gname]
      (c/new-group admin-ph gname))

  (PUT  "/new-rekey"                     [phone chat rk]
      (c/new-rekey phone chat rk)))

; Wraps the router in the middleware that allows parameter destructuring
(def app (wrap-params router))

; Main function. Starts the server in port 8080
(defn -main [& args]
  (println "HTTP server in port 8080.")
  (run-server app {:port 8080}))
