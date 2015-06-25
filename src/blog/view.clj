(ns blog.view
  (:require [clj-time.format :refer [formatter unparse with-locale formatters]]
            [hiccup.core :refer :all]
            [hiccup.page :refer :all]
            [blog.layout :as layout])
  (:import [java.util Locale]))

(def short-date-format  (with-locale (formatter "d. MMMM ") (Locale. "cs")))
(def long-date-format  (with-locale (formatter "dd. MM. yyyy ") (Locale. "cs")))
(defn- short-date [date] (unparse short-date-format date))
(defn- long-date [date] (unparse long-date-format date))
(defn- link [href name] [:a {:href href} name])
(defn- post-url [post] (str "/posts/" (:file-name post)))
(defn- post-absolute-url [post] (str (layout/domain-name) (post-url post)))

(def texts
  { :archiv "Starší články"
    :hard-link "Trvalý odkaz na článek" })

(defn tweet-button [post]
  [:a {:class "twitter-share-button"
       :data-url (post-absolute-url post)
       :data-via "jirkapenzes"
       :href (post-absolute-url post) } "Tweet"])

(defn facebook-button [post]
  [:a {:class "fb-like"
         :data-href (post-absolute-url post)
         :data-layout "button_count" :data-action "like"
         :data-show-faces "true" :data-share "true"
         }])

(defn post [post & active]
  [:div {:class "post"}
   [:h1 (if active
          (link (post-url post) (:title post))
          (:title post))]
   [:div {:class "post-info"}
    (str (short-date (:publish-date post)) " by " (:author post)) ]
   [:div (:body post)]
   [:div {:class "social-share"}
    (tweet-button post)
    (facebook-button post)]])

(defn archiv-post [post]
  [:div {:class "archiv-post"}
   [:h1
    [:span {:class "archiv-date" } (long-date (:publish-date post)) ]
    (link (post-url post) (:title post))]])

(defn- discussion [post] [:p ""])
(defn- social-share [post] [:p ""])

(defn full-post [p]
  [:section
   (post p)
   (social-share p)
   (discussion p)])

(defn posts [posts]
  [:section
   (let [top-post (first posts)]
     [:section {:class "post"}
      (post top-post :active)
      (link (post-url top-post) (:hard-link texts))])

   [:div {:id "archiv"}
    [:h2 (:archiv texts)]
    (map archiv-post (next posts))]])
