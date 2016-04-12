(defproject reago "0.1.0-SNAPSHOT"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.8.40"]
                 [com.taoensso/timbre "4.3.1"]
                 [devcards "0.2.1-6"]
                 [reagent "0.6.0-alpha"]]
  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-figwheel "0.5.2"]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    "target"]
  :aliases {"dev"     ["figwheel" "dev" "devcards"]
            "release" ["cljsbuild" "once" "release"]}

  :source-paths ["src" "test"]

  :cljsbuild {:builds
              [{:id "devcards"
                :source-paths ["src" "test"]
                :figwheel {:devcards true}
                :compiler {:main       "reago.all-tests"
                           :asset-path "js/compiled/devcards_out"
                           :output-to  "resources/public/js/compiled/reago_cards.js"
                           :output-dir "resources/public/js/compiled/devcards_out"
                           :source-map-timestamp true}}
               {:id "dev"
                :source-paths ["src"]
                :figwheel true
                :compiler {:main       "reago.core"
                           :asset-path "js/compiled/out"
                           :output-to  "resources/public/js/compiled/reago.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true}}
               {:id "release"
                :source-paths ["src"]
                :compiler {:main          "reago.core"
                           :externs       []
                           :asset-path    "js/compiled/out"
                           :output-to     "resources/public/js/compiled/reago.js"
                           :optimizations :advanced}}]}
  :figwheel {:css-dirs ["resources/public/css"]})
