(ns dotmoney.components
  (:require [cljs.core.async :refer (put!)]))

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

(defn wallet-form [EVENTCHANNEL wallet active-wallet]
      [:div.wallet-input-container {}
      [:p.wallet-title {} "Wallet Address "]
      [:input.wallet-input {:type "text"
                      :value wallet
                      :on-change (fn [event]
                                   (put! EVENTCHANNEL [:wallet-input {:wallet js/event.target.value}]))}]
        [:div.submit-container {}
          [:div.submit-button {}
            [:p.submit-text
            {:on-click (fn [event](put! EVENTCHANNEL [:submit-wallet {:wallet wallet}]))}
             "Submit"]]]
       [:p {} (if(= active-wallet "") "Verify: " active-wallet)]
       ])

(defn transaction-row [date usd eth eth-price gain-loss]
  [:div.row-container {}
    [:div.row {:class "row1"} date]
    [:div.row {:class "row2"} usd]
    [:div.row {:class "row3"} eth]
    [:div.row {:class "row4"} eth-price]
   ]
  )

  (defn transaction-list [EVENTCHANNEL transactions]
    [:div.transactions-container {}
        (for [transaction transactions]
          ^{:key transaction}
                [transaction-row 
                    (:txId transaction)
                    (:direction transaction)
                    (:usd transaction)
                    (:amount transaction)
                    ])])
