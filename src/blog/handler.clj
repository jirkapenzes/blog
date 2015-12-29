(ns blog.handler
  (:gen-class :main true)
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

(defn published? [post]
  (= true (:published post)))

(defn last-posts []
  (->> (posts/find-all)
       (filter published?)
       (sort-by :publish-date)
       (reverse)))

(defn posts-by-tag [tag]
  (->> (posts/find-by-tag tag)
       (sort-by :publish-date)
       (reverse)))

(defn default-page []
  (->> (last-posts)
       (view/posts)
       (layout/render)))

(defn post-page [post-name]
  (->> (posts/find-by-name post-name)
       (view/full-post)
       (layout/render)))

(defn tag-page [tag]
  (->> (posts-by-tag tag)
       (view/post-list (str "Tag: " tag))
       (layout/render)))

(def rss (rss/channel-xml
          {:title "jirkapenzes'blog" :link "http://blog.penzes.cz/"
           :description "Don’t talk … just do it!"}
          (map #(hash-map
                 :title (:title %)
                 :description (:body %)) (last-posts))))

(defroutes app-routes
  (GET "/" [] (default-page))
  (GET "/posts/:post-name" [post-name] (post-page post-name))
  (GET "/tags/:tag" [tag] (tag-page tag))
  (GET "/rss.xml" [] (response rss))
  (route/resources "/")
  (route/not-found (default-page)))

(def app
  (wrap-defaults app-routes site-defaults))

(defn -main [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (run-server app {:port port})
    (println (str "Listening on port " port))))
