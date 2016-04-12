(ns reago.board
  (:require [reagent.core :as reagent]))

(defn line [x1 y1 x2 y2 & {:as opts}]
  [:line (merge
          {:x1 x1
           :y1 y1
           :x2 x2
           :y2 y2
           :stroke :black}
          opts)])

(def stone-urls {:black "img/black_small.png"
                 :white "img/white_small.png"})

(defn stone [x y w color]
  [:image
   {:xlink-href  (stone-urls color)
    :x x
    :y y
    :width w
    :height w
    :style
    {:filter "url(#shadow)"}}])


;; Necessary until reagent supports react 15.0
(defn ugly-filter [offset]
  (str
   "<feOffset result=\"offOut\" in=\"SourceAlpha\" dx=\"" offset
   "\" dy=\"" offset "\" /> <feGaussianBlur result=\"blurOut\" in=\"offOut\" stdDeviation=\"" 6 "\" /> <feBlend in=\"SourceGraphic\" in2=\"blurOut\" mode=\"normal\" />"))

(defn drop-shadow [offset]
  [:filter {:id "shadow" :x "0" :y "0" :width "200%" :height "200%"
            :dangerouslySetInnerHTML {:__html (ugly-filter offset)}}])

(defn range-scale [values [start stop]]
  (let [dx (/ (- stop start) (count values))]
    (zipmap values (map int (range (/ dx 2) stop dx)))))

(defn make [size width state]
  (let [s (range-scale (range size) [0 width])
        stone-width (/ width (inc size))
        p #(- (s %) (/ stone-width 2))
        w (str stone-width "px")]
    (fn []
      [:div {:style {:background-image "url(/img/wood2.jpg)"
                     :width width
                     :height width}}
       [:svg {:width width
              :height width}
        [:defs
         [drop-shadow (int (/ stone-width 8))]]
        [:g
         (for [x (range size)]
           ^{:key x}
           [line (s x) (s 0) (s x) (s (dec size))])
         (for [y (range size)]
           ^{:key y}
           [line (s 0) (s y) (s (dec size)) (s y)])]
        (doall
         (for [[[x y] color] (:stones @state)]
           ^{:key (str x "-" y)}
           [stone (p x) (p y) w color]))]])))
