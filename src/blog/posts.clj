(ns blog.posts
  (:use markdown.core)
  (:require [clojure.java.io :as io]
            [clj-time.format :refer (formatter parse)]
            [clojure.string :refer (trim split)])
  (:import java.io.File))

(def posts-directory (io/file "resources/posts/"))
(def posts-files (filter #(not (or (.isDirectory %) (= (.getName %) ".DS_Store")))
                         (file-seq posts-directory)))

(def post-definition
  #{ :title :publish-date :tags :body })

(defn- exist-file? [file] (and (instance? File file) (.isFile file)))
(defn- find-file [file-name] (first (filter #(= (.getName %) file-name) posts-files)))
(defn- parse-section [line] (post-definition (keyword (first (split line #":")))))
(defn- parse-content [line] (str (first (merge (subvec (split line #":") 1)))))

(defn- parse-line [line]
  (let [section (parse-section line)]
    (if (keyword? section)
      [section (parse-content line)]
      [nil line])))

(defn- parse-file [file]
  (if (exist-file? file)
    (with-open [reader (io/reader file)]
      (loop [post { :file-name (.getName file) }
             lines (line-seq reader)]
        (if (empty? lines)
          post
          (let [[section content] (parse-line (first lines))]
            (if (contains? post :body)
              (recur (update-in post [:body] #(str % "\n" content)) (rest lines))
              (recur (assoc-in post [section] content) (rest lines)))))))))

(def postprocessing-fn
  { :title trim
    :tags #(set (map trim (split % #",")))
    :publish-date #(parse (formatter "dd.MM.yyyy") (trim %))
    :body md-to-html-string
    :file-name identity })

(defn- add-author [post]
  (assoc-in post [:author] "Jirka Pénzeš"))

(defn postprocessing [post]
  (add-author
   (into (hash-map)
         (for [[section content] post]
           { section ((section postprocessing-fn) content) }))))

(defn- load-post [file]
  (-> (dissoc (parse-file file) nil)
      (postprocessing)))

(defn find-all []
  (map load-post posts-files))

(defn find-by-name [name]
  (load-post (find-file name)))

(defn find-by-tag [tag]
  (->> (find-all)
       (filter #(contains? (:tags %) tag))))
