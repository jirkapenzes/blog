(ns blog.posts
  (:use markdown.core)
  (:require [clojure.java.io :as io]
            [clj-time.format :refer (formatter parse)]
            [clojure.string :refer (trim split replace)])
  (:import [java.io File]
           [java.io BufferedReader StringReader]))

(def posts-directory (io/file "resources/posts/"))
(def posts-files (filter #(not (or (.isDirectory %) (= (.getName %) ".DS_Store")))
                         (file-seq posts-directory)))

(defn- exist-file? [file] (and (instance? File file) (.isFile file)))
(defn- find-file [file-name] (first (filter #(= (.getName %) file-name) posts-files)))
(defn- add-author [post] (assoc-in post [:author] "Jirka Pénzeš"))

(defn- parse-header-line [line]
  (let [p (re-find #"([\S]+):([\s\S]+)" line)]
    { (keyword (get p 1)) (get p 2)}))

(defn postprocessing [post]
  (add-author
   (into (hash-map)
         (for [[section content] post]
           { section (case section
                       :title (trim content)
                       :tags (set (map trim (split content #",")))
                       :publish-date (parse (formatter "dd.MM.yyyy") (trim content))
                       :body (md-to-html-string content)
                       content)}))))

(defn parse-header [header]
  (->> (line-seq (BufferedReader. (StringReader. header)))
       (map parse-header-line)
       (into {})))

(defn parse-file [file]
  (let [p (re-find #"---[\s]+([\s\S]+)---\s([\s\S]+)" (slurp file))]
    {:header (get p 1)
     :body (get p 2)}))

(defn load-post [file]
  (-> (let [p (parse-file file)]
        (into (parse-header (:header p)) {:body (:body p)}))
      (into {:file-name (replace (.getName file) #"\.[^.]+$" "")})
      (postprocessing)))

(defn find-all []
  (map load-post posts-files))

(defn find-by-tag [tag]
  (->> (find-all)
       (filter #(contains? (:tags %) tag))))


(defn find-by-name [name]
  (load-post (find-file (str name ".md"))))

