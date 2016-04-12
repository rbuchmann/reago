(ns reago.board-test
  (:require [cljs.test     :as test :refer-macros [is]]
            [devcards.core :as dc :refer-macros [defcard deftest defcard-rg]]
            [reagent.core  :as reagent]
            [reago.board   :as board]))


(defcard-rg example-board
  [board/make 19 900 (reagent/atom {:stones {[3 3] :white
                                             [2 2] :white
                                             [3 2] :white
                                             [7 2] :white
                                             [2 3] :black
                                             [1 2] :black
                                             [1 4] :black
                                             [2 5] :black}})])


(defcard-rg example-board
  [board/make 19 400 (reagent/atom {:stones {[3 3] :white
                                             [2 2] :white
                                             [3 2] :white
                                             [7 2] :white
                                             [2 3] :black
                                             [1 2] :black
                                             [1 4] :black
                                             [2 5] :black}})])


(defcard-rg filter-test
  [:svg {:height 120 :width 120}
   [:defs
    [:filter {:id "foo"
              :x "0"
              :y "0"
              :width "200%"
              :height "200%"}
     [:feOffset {:result "offOut"
                 :in "SourceGraphic"
                 :dx "30"
                 :dy "30"}]
     [:feBlend {:in "SourceGraphic"
                :in2 "offOut"
                :mode "normal"}]]]
   [:rect {:width "90" :height "90" :stroke :green :stroke-width "3" :fill :yellow :style {:filter "url(#foo)"}}]])
