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
   "\" dy=\"" offset "\" /> <feGaussianBlur result=\"blurOut\" in=\"offOut\" stdDeviation=\"" 7 "\" /> <feBlend in=\"SourceGraphic\" in2=\"blurOut\" mode=\"normal\" />"))

(defn drop-shadow [offset]
  [:filter {:id "shadow" :x "-50%" :y "-50%" :width "400%" :height "400%"
            :dangerouslySetInnerHTML {:__html (ugly-filter offset)}}])

(defn range-scale [values [start stop]]
  (let [dx (/ (- stop start) (count values))]
    (zipmap values (map int (range (/ dx 2) stop dx)))))

(defn cartesian-product [r]
  (for [x r
        y r]
    [x y]))

(defn handicap-point-locations [size]
  (case size
    19 (cartesian-product (range 3 19 6))
    9 (conj (cartesian-product (range 2 9 4))
            [4 4])))

(defn stones [pos-fn stones width]
  [:g
   (doall
    (for [[[x y] color] (sort stones)]
      ^{:key (str x "-" y)}
      [stone (pos-fn x) (pos-fn y) width color]))])

(defn lines [scale size]
  [:g
   (for [x (range size)]
     ^{:key x}
     [line (scale x) (scale 0) (scale x) (scale (dec size))])
   (for [y (range size)]
     ^{:key y}
     [line (scale 0) (scale y) (scale (dec size)) (scale y)])])

(defn handicap-points [size scale stone-width]
  [:g
   (for [[x y] (handicap-point-locations size)]
     ^{:key (str x "-" y)}
     [:circle {:cx (scale x)
               :cy (scale y)
               :r (/ stone-width 10)
               :fill :black}])])

(defn screen-to-point [evt size width]
  (let [stone-width (/ width size)
        t (fn [x] (.floor js/Math (/ x stone-width)))
        svg (.getElementById js/document "board")
        p (.createSVGPoint svg)]
    (set! (.-x p) (.-clientX evt))
    (set! (.-y p) (.-clientY evt))
    (let [transformed (->> svg
                           .getScreenCTM
                           .inverse
                           (.matrixTransform p))]
      [(t (.-x transformed)) (t (.-y transformed))])))

(defn make [size width state handler]
  (let [s (range-scale (range size) [0 width])
        stone-width (/ width (inc size))
        p #(- (s %) (/ stone-width 2))
        w (str stone-width "px")]
    (fn []
      [:div {:style {:background-image "url(/img/wood2.jpg)"
                     :background-size :cover
                     :box-shadow "15px 15px 30px rgba(0, 0, 0, 0.25), inset -3px -3px 6px rgba(0,0,0,0.5), inset 3px 3px 6px rgba(255,255,255,1)"
                     :border-radius "3px"
                     :width width
                     :height width}}
       [:svg {:id "board"
              :width width
              :height width
              :on-click #(handler (screen-to-point % size width))}
        [:defs
         [drop-shadow (int (/ stone-width 20))]]
        [lines s size]
        [handicap-points size s stone-width]
        [stones p (:stones @state) w]]])))
