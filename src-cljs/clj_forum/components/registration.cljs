(ns clj-forum.components.registration
  (:require [reagent.core :refer [atom]]
            [reagent.session :as session]
            [ajax.core :as ajax]
            [clj-forum.components.common :as c]))

(defn register! [fields]
  (ajax/POST "/register"
             {:params @fields
              :handler #()
              :error-handler #()}))

(defn registration-form []
  (let [fields (atom {})]
    (fn []
      [c/modal
       [:div "Picture Gallery Registeration"]
       [:div
        [:div.well.well-sm
         [:strong [:span.glyphicon.glyphicon-asterisk]
          "required field"]]
        [c/text-input "name" :user "enter a user name" fields]
        [c/email-input "email" :email "enter your email" fields true]
        [c/password-input "password" :pass "enter a password" fields]
        [c/password-input "password" :pass-confirm "re-enter the password" fields]]
       [:div
        [:button.btn.btn-danger
         {:on-click #(session/remove! :modal)}
         "Cancel"]
        [:button.btn.btn-primary
        {:on-click #(register! fields)}
        "Register"]]])))
