(defproject pre-message "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [http-kit "2.2.0"]
                 [compojure "1.6.0"]
                 [korma "0.4.3"]
                 [org.xerial/sqlite-jdbc "3.21.0.1"]
                 [nics.crypto/afgh "1.0.1"]]
  :main ^:skip-aot pre-message.core
  :target-path "target/%s"
  :repositories [["localrepo" "file:local-repo"]]
  :profiles {:uberjar {:aot :all}}
  :jvm-opts ["--add-modules" "java.xml.bind"])
