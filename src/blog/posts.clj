(ns blog.posts
  (:use markdown.core)
  (:require [clojure.java.io :as io]
            [clj-time.format :refer (formatter parse)]
            [clojure.string :refer (split-lines trim split)])
  (:import [java.io File]))

(def posts-directory (io/file "resources/posts/"))
(def posts-files (filter #(not (or (.isDirectory %) (= (.getName %) ".DS_Store")))
                         (file-seq posts-directory)))

(defn- exist-file? [file] (and (instance? File file) (.isFile file)))
(defn- find-file [file-name] (first (filter #(= (.getName %) file-name) posts-files)))
(defn- add-author [post] (assoc-in post [:author] "Jirka Pénzeš"))
(def truthy? #{"true"})

(defn- parse-header-line [line]
  (let [p (re-find #"([\S]+):([\s\S]+)" line)]
    { (keyword (get p 1)) (get p 2)}))

(defn postprocessing [post]
  (add-author
   (into (hash-map)
         (for [[section content] post]
           { section (case section
                       :title (trim content)
                       :published (if (truthy? (trim content)) true false)
                       :tags (set (map trim (split content #",")))
                       :publish-date (parse (formatter "dd.MM.yyyy") (trim content))
                       :body (md-to-html-string content)
                       content)}))))

(defn parse-header [header]
  (->> (map parse-header-line header)
       (into {})))

(defn parse-file [file]
  (let [splitted-file (split-lines (slurp file))]
    {:header  (rest (take 5 (split-lines (slurp file))))
     :body (reduce #(str %1 "\r\n" %2) (drop 6 (split-lines (slurp file))))}))

(defn load-post [file]
  (-> (let [p (parse-file file)]
        (into (parse-header (:header p)) {:body (:body p)}))
      (into {:file-name (clojure.string/replace (.getName file) #"\.[^.]+$" "")})
      (postprocessing)))

(defn find-all []
  (map load-post posts-files))

(defn find-by-tag [tag]
  (->> (find-all)
       (filter #(contains? (:tags %) tag))))

(defn find-by-name [name]
  (load-post (find-file (str name ".md"))))
