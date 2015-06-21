(defproject blog "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://blog.penzes.cz/"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [ring/ring-defaults "0.1.2"]
                 [markdown-clj "0.9.66"]
                 [hiccup "1.0.5"]
                 [clj-rss "0.1.9"]
                 [ring/ring-jetty-adapter "1.3.2"]]

  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler blog.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
