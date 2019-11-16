(ns website.renderers
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.edn :as edn]
            [endophile.core :refer [mp to-clj html-string]]
            [stasis.core :as stasis]
            [website.views :as views]))

(defn ^:private extract-chunks
  "Extract chunks from the source text into a vector, as delimited by boundary."
  [text]
  (let [boundary #"\n\n---\n\n"]
    (str/split text boundary 2)))

(defn ^:private read-settings
  "Return a settings map from edn-formatted text."
  [text]
  (edn/read-string text))

(defn ^:private read-meta
  "Return a metadata map from edn-formatted text."
  [text]
  (edn/read-string text))

(defn ^:private read-content
  "Return HTML-formatted content in a map from markdown-formatted text."
  [text]
  {:content (-> text mp to-clj html-string)})

(defn ^:private get-sources
  "Return a map with raw sources from the file system."
  []
  {:public (stasis/slurp-directory "resources/assets"
                                   #".*\.(css|js|txt|eot|svg|ttf|woff|woff2)$")
   :pages (stasis/slurp-directory "resources/pages" #"\.md$")})

(defn ^:private make-context
  "Process text from a source file and make a context for a view."
  [text]
  (let [[meta content] (extract-chunks text)]
    (merge
     {:view-name :generic} ;; default
     (read-meta meta)
     (read-content content)
     (read-settings (slurp (io/file "src" "website" "config.edn"))))))

(defn ^:private render-page
  "Return a template rendered with a given context extracted from text."
  [text]
  (views/layout (make-context text)))

(defn ^:private render-public
  "Return a map of rendered public sources, for ingestion by stasis."
  [sources]
  sources)

(defn ^:private render-pages
  "Return a map of templates rendered with a given context, for ingestion by stasis."
  [texts]
  (zipmap (map #(str/replace % #"\.md$" ".html") (keys texts))
          (map render-page (vals texts))))

(defn render-website
  "Return a map with all website assets, for ingestion by stasis."
  []
  (let [sources (get-sources)]
    {:public (render-public (:public sources))
     :pages (render-pages (:pages sources))}))
