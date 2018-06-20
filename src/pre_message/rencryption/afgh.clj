(ns pre-message.rencryption.afgh
  (:require [clojure.string :refer [split]])
  (:import (java.util Base64))
  (:import (nics.crypto.proxy.afgh AFGHGlobalParameters AFGHProxyReEncryption)))

(def rBits 256)
(def qBits 1536)

;; Macro that builds the global parameters at compile time.
(defmacro mac-global []
  (time (str (AFGHGlobalParameters. rBits qBits))))

;; String that represents the global parameters
(def global-str (mac-global))

;; Global parameters.
(time (def global (AFGHGlobalParameters. global-str)))

;; Re-encryption function.
(defn afgh-reencrypt [c rk]
  (AFGHProxyReEncryption/reEncryption c rk global))

; Decode Base64 string to byte array
(defn decode [encoded]
  (as-> encoded e
        (split e #"\n")
        (apply str e)
        (.decode (Base64/getDecoder) e)))

; Encode Base64 string to byte array
(defn encode [byte-arr]
  (.encodeToString (Base64/getEncoder) byte-arr))

; Reencrypt a message in Base64 representation
(defn reencrypt [message rk]
  (-> message
      decode
      (afgh-reencrypt rk)
      encode))

; Reencrypt only if necessary
(defn recrypt-necessary [text rk]
  (if (some? rk)
    (reencrypt text (decode rk))
    text))
