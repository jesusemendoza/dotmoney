(ns dotmoney.core
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [reagent.core :as reagent]
            [dotmoney.components :refer (header items-list wallet-input)]
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
  :update-wallet (fn [previous-wallet]
                        (let [{wallet :wallet} previous-wallet]
                          (println wallet)
                        (swap! app-state assoc-in [:wallet] wallet )))})


(go
 (while true
   (let [[event-name event-data] (<! EVENTCHANNEL)]
     ((event-name EVENTS) event-data))))

(defn app []
  [:div {:class "app-container"}
    [header (:message @app-state )]
    [wallet-input EVENTCHANNEL (:wallet @app-state)]
    [items-list EVENTCHANNEL (:items @app-state) (:active-item @app-state)]
   ])

(reagent/render [app] (js/document.querySelector "#app") )
