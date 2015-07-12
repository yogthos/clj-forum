(ns clj-forum.components.login
  (:require [reagent.core :refer [atom]]
            [reagent.session :as session]
            [clojure.string :as string]
            [goog.crypt.base64 :as b64]
            [ajax.core :as ajax]))

(defn auth-hash [user pass]
  (->> (str user ":" pass) (b64/encodeString) (str "Basic ")))

(defn login! [user pass error]
  (cond
    (and @user @pass)
    (ajax/POST "/login"
               {:headers       {"Authorization" (auth-hash (string/trim @user) @pass)
                                "Accept"        "application/transit+json"
                                "x-csrf-token"  js/csrfToken}
                :handler       #(session/put! :identity %)
                :error-handler #(reset! error (get-in % [:response :error]))})
    (empty? @user)
    (reset! error "user name is required")
    (empty? @pass)
    (reset! error "password is required")))

(defn input [type placeholder value]
  [:input
   {:type        type
    :value       @value
    :on-change   #(reset! value (-> % .-target .-value))
    :placeholder placeholder}])

(defn login-component []
  (let [user (atom nil)
        pass (atom nil)
        error (atom nil)]
    (fn []
      [:div
       [input :text "user id" user]
       [input :password "password" pass]
       (when-let [error @error]
         [:p.alert.alert-danger error])
       [:button.btn.btn-primary
        {:on-click #(login! user pass error)}
        "login"]])))

(defn logout-component []
  [:button.btn.btn-primary
   {:on-click #(session/remove! :identity)}
   "logout"])
