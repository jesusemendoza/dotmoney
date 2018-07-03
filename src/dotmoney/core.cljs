(ns dotmoney.core
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [reagent.core :as reagent]
            [dotmoney.communication :refer (wallet-request)]
            [dotmoney.components :refer (header items-list wallet-form transaction-row transaction-list)]
            [cljs.core.async :refer (chan put! <!)]))

;allows println to generate a console.log in the browser console
(enable-console-print!)
 
;similar to the main state used in react
(defonce app-state
 (reagent/atom
  {:message "dotMoney"
   :wallet "0xc1A30fd9f85D48c38a8f8733d450D059B7BbA1B5"
   :active-wallet ""
   :transactions []
   :items [{:display "item1"}
           {:display "item2"}
           {:display "item3"}
           {:display "item4"}
           {:display "item5"}]
   :active-item {}}))
 
(def EVENTCHANNEL (chan))


(def EVENTS
 {:update-active-item (fn [{:keys [active-item]}]
                        (swap! app-state assoc-in [:active-item] active-item))
  :wallet-input (fn [value]
                    (let [{wallet :wallet} value]
                        (swap! app-state assoc-in [:wallet] wallet)))
  :submit-wallet (fn [value]
                   (let [{wallet :wallet} value]
                        (wallet-request EVENTCHANNEL wallet)))
  :update-transactions (fn [value]
                 (println value)
                 (swap! app-state assoc-in [:transactions] value)
                ;  (let [{transactions :transactions} value]
                ;    (swap! app-state assoc-in [:transactions] (.parse js/JSON transactions))
                ;    (println transactions))
                         )    
  })

(go
 (while true
   (let [[event-name event-data] (<! EVENTCHANNEL)]
     ((event-name EVENTS) event-data))))




(defn app []
  [:div {:class "app-container"}
    [header (:message @app-state )]
    [wallet-form EVENTCHANNEL (:wallet @app-state)]
    [transaction-row "ID" "I/O" "USD / 1 ETH" "ETH Amount"]
    [transaction-list EVENTCHANNEL (:transactions @app-state)]
    ; [items-list EVENTCHANNEL (:items @app-state) (:active-item @app-state)]
   ])

(reagent/render [app] (js/document.querySelector "#app") )
