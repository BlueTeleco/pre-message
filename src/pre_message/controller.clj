(ns pre-message.controller
  (:import (java.util Base64))
  (:require [pre-message.rencryption.afgh :as afgh]
            [pre-message.model :as m]))

;; Utils

; Decode Base64 string to byte array
(defn decode [encoded]
  (.decode (Base64/getDecoder) encoded))

; Encode Base64 string to byte array
(defn encode [byte-arr]
  (.encodeToString (Base64/getEncoder) byte-arr))

; Reencrypt a message in Base64 representation
(defn reencrypt [message rk]
  (-> message
      decode
      (afgh/reencrypt (decode rk))
      encode))

; Reencrypt only if necessary
(defn recrypt-necessary [text rk]
  (if (some? rk)
    (reencrypt text rk)
    text))

;;;; Controllers

;; Get global variables
(defn global []
  afgh/global-str)

;; Get chats from user
(defn chats [phone]
  (->> (m/select-chats! phone)
       (map #(str "" (:id %) 
                  ":" (:name %) 
                  "\n"))
       (apply str)))

;; Get public key of user
(defn pubkey-user [phone]
  (-> phone
      m/select-user!
      :pubKey
      str))

;; Get public key of admin
(defn pubkey-admin [chat]
  (-> chat
      m/select-admin!
      m/select-by-id!
      :pubKey
      str))

;; Get reencryption key between a user and the
;; admin of a chat
(defn rekey-users [phone chat]
  (let [admin (m/select-admin! chat)
        user (:id (m/select-user! phone))]
    (if (not= user admin)
      (and (not= user admin)
        (-> (m/select-rekey! user admin)
            :reKey
            str))
      (str ""))))

;; Sincronize the group messages
(defn sinc-group [chat phone order]
  (let [user (:id (m/select-user! phone))
        admin (m/select-admin! chat)]
    (->> (m/select-messages! chat order)
         (map #(str ""  (:name (m/select-by-id! (:sender %))) 
                    ":" (recrypt-necessary (:text %) (m/select-rekey! admin user)) 
                    "<--->"))
         (apply str))))

;; Add new user to a certain group
(defn user->group [group phone rk]
  (and (some? (m/select-user! phone))
       (m/add-to-group! group phone rk)))

;; Send a message to a group
(defn message->group [text group phone]
  (let [admin (m/select-admin! group)
        sender (:id (m/select-user! phone))
        rk (m/select-rekey! sender admin)]
      (-> text
          (recrypt-necessary rk)
          (m/add-message! group sender))))

;; Create new user
(defn new-user [uname phone pk]
  (m/add-user! uname phone pk))

;; Create new group
(defn new-group [admin gname]
  (m/add-group! admin gname))

;; Create new reencryption key
(defn new-rekey [phone chat rk]
  (let [user (m/select-user! phone)
        admin (m/select-admin! chat)]
    (m/add-rekey! user admin rk)))
