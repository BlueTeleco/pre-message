(ns pre-message.controller
  (:import (java.util Base64))
  (:require [pre-message.rencryption.afgh :as afgh]
            [pre-message.model :as m]))

;; Get global variables
(defn global []
  afgh/global-str)

;; Get chats from user
(defn chats [phone]
  (->> (m/select-chats! phone)
       (map #(str "" (:id %) ":" (:name %) "\n"))
       (apply str)))

;; Get public key of user
(defn pubkey-user [phone]
  (-> phone
      m/select-user!
      :pubKey
      str))

;; Get public key of admin
(defn pubkey-admin [chat]
  (-> phone
      m/select-admin!
      m/select-by-id!
      :pubKey
      str))

;; Get reencryption key between a user and the
;; admin of a chat
(defn rekey-users [phone chat]
  (let [admin (m/select-admin! chat)
        user (:id (m/select-user! phone))]
    (and (not= user admin)
       (-> (m/select-rekey! user admin)
           :reKey
           str))))

;; Create new user
(defn new-user [uname phone pk]
  (m/add-user! uname phone pk))

;; Create new group
(defn new-group [admin gname]
  (m/add-group! admin gname))

;; Create new reencryption key
(defn new-rekey [phone chat rk]
  (let [user (select-user! phone)
        admin (select-admin! chat)]
    (add-rekey! user admin rk)))

;; Add new user to a certain group
(defn user->group [group phone rk]
  (and (some? (m/select-user! phone))
       (m/add-to-group! group phone rk)))

;; Send a message to a group
(defn message->group [text group phone]
  (let [admin (m/select-admin! group)
        sender (m/select-user! phone)
        rk (m/select-rekey! sender admin)]
    (do
      ()
      (m/add-message! text group sender))))

;; Sincronize the group messages
(defn sinc-group [id order]
  (m/select-messages! id order))

;; Utils

; Decode Base64 string to byte array
(defn decode [encoded]
  (doto (Base64/getDecoder)
        (.decode encoded)))

; Encode Base64 string to byte array
(defn encode [byte-arr]
  (doto (Base64/getEncoder)
        (.encodeToString byte-arr)))
