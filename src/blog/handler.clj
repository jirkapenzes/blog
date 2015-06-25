(ns blog.handler
  (:use ring.util.response
        org.httpkit.server)
  (:require [blog.posts :as posts]
            [blog.view :as view]
            [blog.layout :as layout]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [clj-rss.core :as rss]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn last-posts [n]
  (->> (posts/find-all)
       (sort-by :publish-date)
       (take-last n)
       (reverse)))

(defn default-page []
  (->> (last-posts 10)
       (view/posts)
       (layout/render)))

(defn post-page [post-name]
  (->> (posts/find-by-name post-name)
       (view/full-post)
       (layout/render)))

(def rss (rss/channel-xml
          {:title "jirkapenzes'blog" :link "http://blog.penzes.cz/"
           :description "Don’t talk … just do it!"}
          (map #(hash-map
                 :title (:title %)
                 :description (:body %)) (last-posts 10))))

(defroutes app-routes
  (GET "/" [] (default-page))
  (GET "/posts/:post-name" [post-name] (post-page post-name))
  (GET "/rss.xml" [] (response rss))
  (route/resources "/")
  (route/not-found (default-page)))

(def app
  (wrap-defaults app-routes site-defaults))

(defn -main [& args]
  (let [port (Integer/parseInt (get (System/getenv) "OPENSHIFT_CLOJURE_HTTP_PORT" "8080"))]
    (let [ip (get (System/getenv) "OPENSHIFT_CLOJURE_HTTP_IP" "0.0.0.0")]
  (run-server app {:ip ip :port port}))))