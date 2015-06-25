(ns blog.layout
  (:require [hiccup.core :refer :all]
            [hiccup.page :refer :all]))

(def includes [(include-js "//ajax.googleapis.com/ajax/libs/webfont/1.5.18/webfont.js")
               (include-js "/js/google-analytics.js")
               (include-js "../js/webfont.js")
               (include-js "../js/highlight.pack.js")\
               (include-js "../js/twitter-share-button.js")
               (include-js "../js/facebook-share-button.js")
               (include-js "../js/disqus.js")
               (include-css "//fonts.googleapis.com/css?family=Trykker")
               (include-css "../css/pure-min.css")
               (include-css "../css/highlightjs-github.css")
               (include-css "../css/screen.css")])

(def blog { :title "jirkapenzes'blog"
            :links [["GitHub" "https://github.com/jirkapenzes" "../img/icon-github.png"]
                    ["Twitter" "https://twitter.com/jirkapenzes" "../img/icon-twitter.png"]
                    ["LinkedIn" "http://www.linkedin.com/in/jirkapenzes" "../img/icon-linkedin.png"]
                    ["Instagram" "http://www.instagram.com/jirkapenzes" "../img/icon-instagram.png"]
                    ["Rss" "/rss.xml" "../img/icon-rss.png"]]
            :url "http://blog.penzes.cz"})

(defn domain-name [] (:url blog))

(defn- render-head []
  `[:head
    [:title ~@(:title blog)]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
    ~@includes
    [:script "hljs.initHighlightingOnLoad();"]])

(defn make-icon-link [[title href src]]
  [:a {:href href} [:img {:alt title :src src}]])

(defn- render-body [& content]
  [:body
   [:div {:id "header" :class "pure-g-r"}
    [:div {:class "pure-u-1 pure-u-sm-1-2"}
     [:a {:href "/"} [:h1 (:title blog)]]]
    [:div {:id "social-links" :class "pure-u-1 pure-u-sm-1-2"}
     (map make-icon-link (:links blog))]]
   [:div {:id "fb-root"}]
   [:div {:id "content"}
    content]
   [:div {:id "footer"}
    "by jirkapenzes 2015 (c)"]])

(defn render [& content]
  (html5 { :lang "en" }
         (render-head)
         (render-body content)))
