(ns dotmoney.core
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [reagent.core :as reagent]
            [dotmoney.components :refer (header items-list wallet-form transaction-row)]
            [cljs.core.async :refer (chan put! <!)]))

(enable-console-print!)

(println "This text is printed from src/dotmoney/core.cljs. Go ahead and edit it and see reloading in action.")

(def EVENTCHANNEL (chan))

;similar to the main state used in react
(defonce app-state
 (reagent/atom
  {:message "dotMoney"
   :wallet "0x"
   :items [{:display "item1"}
           {:display "item2"}
           {:display "item3"}
           {:display "item4"}
           {:display "item5"}]
   :active-item {}}))

(def EVENTS
 {:update-active-item (fn [{:keys [active-item]}]
                        (swap! app-state assoc-in [:active-item] active-item))
  :update-wallet (fn [value]
                        (let [{wallet :wallet} value]
                        (swap! app-state assoc-in [:wallet] wallet)))
  :submit-wallet (fn [value]
                   (println "yay it was submited"))
  })


(go
 (while true
   (let [[event-name event-data] (<! EVENTCHANNEL)]
     ((event-name EVENTS) event-data))))

(defn app []
  [:div {:class "app-container"}
    [header (:message @app-state )]
    [wallet-form EVENTCHANNEL (:wallet @app-state)]
    [transaction-row "12/23/2017" "46.78" "0.003" "1559.33" "-58.99"]
    ; [items-list EVENTCHANNEL (:items @app-state) (:active-item @app-state)]
   ])

(reagent/render [app] (js/document.querySelector "#app") )
