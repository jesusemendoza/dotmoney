(ns dotmoney.communication
  (:require [ajax.core :refer [GET POST]]
            [cljs.core.async :refer (put!)]))

(defn wallet-request [EVENTCHANNEL wallet-id]
     ;async stuff
    (defn error-handler [{:keys [status status-text]}]
    (println "error"))
    (POST "https://dot-money-backend.herokuapp.com/api/v1/wallet"
    ;   (POST "http://localhost:3000/api/v1/wallet"
        {:params {:wallet wallet-id}
        :handler (fn [response](put! EVENTCHANNEL [:update-transactions response]))
        :format :json
        :response-format :json
        :keywords? true
        :error-handler error-handler})
    )  