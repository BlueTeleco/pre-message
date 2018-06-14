(ns pre-message.controller
  (:require [pre-message.rencryption.afgh :as afgh]
            [pre-message.model :as m]))

;; Get global variables
(defn global []
  afgh/global-str)

;; Get chats from user
(defn chats [phone]
  (apply str
    (map #(str "" (:id %) ":" (:name %) "\n")
         (m/select-chats! phone))))

;; Get public key of user
(defn pubkey-user [phone]
  (str (:pubKey (m/select-user! phone))))

;; Create new user
(defn new-user [uname phone pk]
  (m/add-user! uname phone pk))

;; Create new group
(defn new-group [admin gname]
  (and (m/add-group! admin gname)
       (str "New group!! " admin " is Admin\n")))

;; Add new user to a certain group
(defn user->group [group phone rk]
  (and (some? (m/select-user! phone))
       (m/add2group! group phone rk)))

;; Send a message to a group
(defn message->group [text group sender]
  (m/add-message! text group sender))

;; Sincronize the group messages
(defn sinc-group [id order]
  (m/select-messages! id order))
