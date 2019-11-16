(ns website.core
  (:gen-class)
  (:require [environ.core :refer [env]]
            [stasis.core :as stasis]
            [website.renderers :refer [render-website]]))

(defn get-pages
  "Return a map of pages that stasis can consume to produce the site."
  []
  (stasis/merge-page-sources (render-website)))

(defn export []
  (let [target-dir (env :target-dir)]
    (stasis/empty-directory! target-dir)
    (stasis/export-pages (get-pages) target-dir)))

(def handler (stasis/serve-pages get-pages))
