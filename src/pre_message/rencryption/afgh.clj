(ns pre-message.rencryption.afgh
  (:import (nics.crypto.proxy.afgh AFGHGlobalParameters AFGHProxyReEncryption)))

(def rBits 256)
(def qBits 1536)

;; Macro that builds the global parameters at compile time.
(defmacro mac-global []
  (time (str (AFGHGlobalParameters. rBits qBits))))

;; Global parameters.
(time (def global (AFGHGlobalParameters. (mac-global))))

;; Re-encryption function.
(defn reencrypt [c rk]
  (let [e-ppp (.. global getE (pairing rk))]
  (AFGHProxyReEncryption/reEncryption c rk e-ppp)))
