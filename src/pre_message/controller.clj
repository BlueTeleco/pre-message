(ns pre-message.controller
  (:require [pre-message.rencryption.afgh :as afgh]
            [pre-message.model :as m]))

;; Get global variables
(defn global []
  (afgh/mac-global))

;; Create new user
(defn new-user [uname phone pk]
  (and (m/add-user! uname phone pk)
       (str "New user!! Hello " uname "\n")))

;; Create new group
(defn new-group [admin & others]
  (and (m/add-group! admin)
       (map m/add2group! (cons admin others))
       (str "New group!! " admin " is Admin\n")))

;; Add new user to a certain group
(defn user->group [group phone]
  (and (m/add2group! group phone)
       (str "Added to " group "without trouble!!\n")))

;; Send a message to a group
(defn message->group [id]
  (str "Sent new message to group " id " \n"))

;; Sincronize the group messages
(defn sinc-group [id order]
  (m/select-group id order))
