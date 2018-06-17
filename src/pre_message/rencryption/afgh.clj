(ns pre-message.rencryption.afgh
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
(defn reencrypt [c rk]
  (AFGHProxyReEncryption/reEncryption c rk global))
