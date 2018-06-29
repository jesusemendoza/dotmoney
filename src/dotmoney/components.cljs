(ns dotmoney.components
  (:require [cljs.core.async :refer (chan put! <!)]))

(defn header [message]
  [:div {:class "title-container"}
   [:p {:class "title"} message]])

(defn items-list [EVENTCHANNEL items active-item]
 [:div {:class "item-list"}
   (for [item items]
     ^{:key item}
         [:div {:class (if (= active-item item) "item active" "item")}
           [:h1
            {:on-click (fn [event](put! EVENTCHANNEL [:update-active-item {:active-item item}]))}
            (:display item)]])])

; (defn atom-input [wallet EVENTCHANNEL]
;   [:input {:type "text"
;            :value wallet
;            :on-change (fn [event](put! EVENTCHANNEL [:update-wallet {:wallet wallet}]))}])

(defn shared-state [EVENTCHANNEL wallet]
      [:div {}
       [:p {} "The value is now: " wallet]
       [:p "Change it here: "
        [:input {:type "text"
                :value wallet
                :on-change (fn [event](put! EVENTCHANNEL [:update-wallet {:wallet wallet}]))}]]])
