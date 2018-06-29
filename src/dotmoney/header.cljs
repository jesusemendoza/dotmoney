(ns dotmoney.header
  (:require [cljs.core.async :refer (chan put! <!)]))

(defn header [message]
  [:div {}
   [:h1 {:class "header"} message]])

(defn items-list [EVENTCHANNEL items active-item]
 [:div {:class "item-list"}
   (for [item items]
     ^{:key (rand 30)}
         [:div {:class (if (= active-item item) "item active" "item")}
           [:h1
            {:on-click (fn [event](put! EVENTCHANNEL [:update-active-item {:active-item item}]))}
            (:display item)]])])
