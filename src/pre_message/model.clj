(ns pre-message.model
  (:require [korma.core :refer :all]
            [korma.db :refer :all]))

;;; Database
(defdb db (sqlite3 {:db "resources/db/messages.db"}))

;;; Declare before usage
(declare Chats ChatMembers)

;;; Entities
(defentity Users)
(defentity ReEncryptionKeys)
(defentity Chats
  (has-many ChatMembers {:fk :chat}))
(defentity ChatMembers
  (belongs-to Chats {:fk :chat}))
(defentity Messages)

;;; Select

; Select user from their id
(defn select-by-id! [id]
  (first 
    (select Users
      (where {:id id}))))

; Select user from their phone
(defn select-user! [phone]
  (first 
    (select Users
      (where {:phone phone}))))

; Select chats from a user
(defn select-chats! [phone]
  (let [user (:id (select-user! phone))]
    (select ChatMembers
      (with Chats)
      (where {:member user}))))

; Select admin of chat
(defn select-admin! [chat]
  (:admin (first
      (select Chats
        (where {:id chat})))))

; Select reencryption key of two users
(defn select-rekey! [delegator delegatee]
  (first
    (select ReEncryptionKeys
      (where {:delegator delegator, :delegatee delegatee}))))

; Select messages from group
(defn select-messages! [group order]
  (select Messages
    (where (and (= :chat group)
                (> :id order)))))

;;; Insert

; Insert new user in the database
(defn add-user! [uname phone pk]
  (insert Users
    (values {:name uname, :phone phone, :pubkey pk})))

; Insert new rekey in the database
(defn add-rekey! [delegator delegatee rk]
  (insert ReEncryptionKeys
    (values {:delegator delegator, :delegatee delegatee, :reKey rk})))

; Insert new group in the database
(defn add-group! [admin-ph gname]
  (let [admin (:id (select-user! admin-ph))
        chat  (insert Chats
                (values {:admin admin, :name gname}))]
    (insert ChatMembers
      (values {:member admin, :chat (first (vals chat))}))))

; Insert relationship in the database representing a new user
; in the specified group
(defn add-to-group! [group phone rk]
  (let [user (:id (select-user! phone))
        admin (select-admin! group)]
    (do
      (insert ChatMembers
        (values {:member user, :chat group}))
      (add-rekey! admin user rk))))

; Insert message in the database
(defn add-message! [text group sender]
  (insert Messages
    (values {:text text, :chat group, :sender sender})))
