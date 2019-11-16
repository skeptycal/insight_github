(defproject website "1.1.0"
  :description "Using the Github API to analyze Github repo data."
  :url "https://www.skeptycal.com"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [clojure.java-time "0.3.2"]
                 [endophile "0.2.1"]
                 [environ "1.1.0"]
                 [hiccup "1.0.5"]
                 [ring "1.7.1"]
                 [stasis "2.4.0"]]
  :ring {:handler website.core/handler}
  :main ^:skip-aot website.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:plugins [[lein-environ "1.1.0"]
                             [lein-ring "0.12.5"]]
                   :env {:target-dir "output"}}}
  :aliases {"build" ["run" "-m" "website.core/export"]
            "serve" ["ring" "server-headless"]})
