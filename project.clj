(defproject blog "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://blog.penzes.cz/"
  :min-lein-version "2.5.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [ring/ring-defaults "0.1.2"]
                 [markdown-clj "0.9.66"]
                 [hiccup "1.0.5"]
                 [clj-rss "0.1.9"]
                 [ring/ring-jetty-adapter "1.3.2"]
                 [http-kit "2.1.12"]]

  :main blog.handler
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler blog.handler/app}
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                          [ring-mock "0.1.5"]]}
             :uberjar {:aot :all
                       :main blog.handler
                       :env {:production true}}})
