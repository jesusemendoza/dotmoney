(ns dotmoney.core
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [reagent.core :as reagent]
            [dotmoney.components :refer (header items-list wallet-form transaction-row)]
            [cljs.core.async :refer (chan put! <!)]
            [ajax.core :refer [GET POST]]))

;allows println to generate a console.log in the browser console
(enable-console-print!)

(def EVENTCHANNEL (chan))
;async stuff
(defn handler [response]
  (println response))

(defn error-handler [{:keys [status status-text]}]
  (println "error"))

(defn wallet-request [wallet-id]
(POST "http://localhost:3000/api/v1/wallet"
  {:params {:wallet wallet-id}
   :handler handler
   :format :json
   :error-handler error-handler}))


; (defn handler2 [[ok response]]
;   (if ok
;     (.log js/console (str response))
;     (.error js/console (str response))))

; (ajax-request
;  {:uri "http://localhost:3000/api/v1/wallet"
;   :method :post
;   :params {:wallet "0xc1A30fd9f85D48c38a8f8733d450D059B7BbA1B5"}
;   :handler handler2
;   :format (json-request-format)
;   :response-format (json-response-format {:keywords? true})})   
;similar to the main state used in react
(defonce app-state
 (reagent/atom
  {:message "dotMoney"
   :wallet "0xc1A30fd9f85D48c38a8f8733d450D059B7BbA1B5"
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
                   (let [{wallet :wallet} value]
                        (wallet-request wallet)))    
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
