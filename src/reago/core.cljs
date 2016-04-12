(ns reago.core
  (:require [reagent.core :as reagent]))

(defn sample-component []
  [:h1 "Hello, World"])

(defn ^:export run []
  (when-let [dom-node (.getElementById js/document "container")]
    (reagent/render sample-component dom-node)))
