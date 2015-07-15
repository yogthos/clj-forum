(ns clj-forum.ajax
  (:require [ajax.core :as ajax]
            [reagent.session :as session]))

(def transit-format "application/transit+json")

(defn csrf-token []
  (.-value (.getElementById js/document "csrf-token")))

(defn default-error-handler [response]
  (session/put! :error (get-in response [:response :error])))

(defn GET [url opts]
  (ajax/GET url (merge
                  {:headers {"Accept" transit-format}
                   :error-handler default-error-handler}
                  opts)))

(defn POST [url opts]
  (ajax/POST url (merge
                   {:headers
                    {"Accept"               transit-format
                     "x-csrf-token" (csrf-token)}
                    :error-handler default-error-handler}
                   opts)))
