(ns pre-message.model
  (:require [korma.core :refer :all]
            [korma.db :refer :all]))

;;; Database
(defdb db (sqlite3 {:db "resources/db/messages.db"}))

;;; Entities
(defentity Users)
(defentity ReEncryptionKeys)
(defentity Chats)
(defentity ChatMembers)
(defentity Messages)

;;; Utilities

; Get a user from the database from their phone
(defn select-user-phone [phone]
  (first 
    (select Users
      (where {:phone phone}))))

;;; Model functions

; Insert new user in the database
(defn add-user! [uname phone pk]
  (insert Users
    (values {:name uname, :phone phone, :pubkey pk})))

; Insert new group in the database
(defn add-group! [admin-ph]
  (let [admin (:id (select-user-phone admin-ph))]
    (insert Chats
      (values {:admin admin}))))

; Insert relationship in the database representing a new user
; in the specified group
(defn add2group! [group phone]
  (let [user (:id (select-user-phone phone))]
    (insert ChatMembers
      (values {:member user, :chat group}))))

; Insert message in the database
(defn add-message! [text group sender]
  (insert Messages
    (values {:text text, :chat group, :sender sender})))

; Select messages fron group
(defn select-group [group order]
  (select Messages
    (where (and (= :chat group)
                (> :id order)))))
