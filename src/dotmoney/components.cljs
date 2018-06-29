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

(defn wallet-form [EVENTCHANNEL wallet]
      [:div.wallet-input-container {}
       [:p.wallet-title {} "Wallet Address "]
       [:p.wallet-sub "Enter Here: "
        [:input.wallet-input {:type "text"
                        :value wallet
                        :on-change (fn [event]
                                     (put! EVENTCHANNEL [:update-wallet {:wallet js/event.target.value}]))}]]
      ; [:p {} "Verify: " wallet]
       ])

(defn transaction-row [date usd eth eth-price gain-loss]
  [:div.row-container {}
    [:div.row {} date]
    [:div.row {} usd]
    [:div.row {} eth]
    [:div.row {} eth-price]
    [:div.row {} gain-loss]
   ]
  )
