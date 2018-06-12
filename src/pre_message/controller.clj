(ns pre-message.controller
  (:require [pre-message.rencryption.afgh :as afgh]
            [pre-message.model :as m]))

;; Get global variables
(defn global []
  afgh/global-str)

;; Get chats from user
(defn chats [phone]
  (apply str
    (map #(str "" (:id %) ":" (:name %) "-")
         (m/select-chats phone))))

;; Create new user
(defn new-user [uname phone pk]
  (and (m/add-user! uname phone pk)
       (str "New user!! Hello " uname "\n")))

;; Create new group
(defn new-group [admin gname]
  (and (m/add-group! admin gname)
       (str "New group!! " admin " is Admin\n")))

;; Add new user to a certain group
(defn user->group [group phone]
  (and (m/add2group! group phone)
       (str "Added " phone " to chat " group " without trouble!!\n")))

;; Send a message to a group
(defn message->group [text group sender]
  (m/add-message! text group sender))

;; Sincronize the group messages
(defn sinc-group [id order]
  (m/select-group id order))
