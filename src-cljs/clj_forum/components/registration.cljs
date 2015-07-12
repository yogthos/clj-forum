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

(defn input [type id placeholder fields]
  [:input.form-control.input-lg
   {:type type
    :placeholder placeholder
    :value (id @fields)
    :on-change #(swap! fields assoc id (-> % .-target .-value))}])

(defn form-input [type label id placeholder fields optional?]
  [:div.form-group
   [:label label]
   (if optional?
     [input type id placeholder fields]
     [:div.input-group
      [input type id placeholder fields]
      [:span.input-group-addon
       [:span.glyphicon.glyphicon-asterisk]]])])

(defn text-input [label id placeholder fields & [optional?]]
  (form-input :text label id placeholder fields optional?))

(defn email-input [label id placeholder fields & [optional?]]
  (form-input :email label id placeholder fields optional?))

(defn password-input [label id placeholder fields & [optional?]]
  (form-input :password label id placeholder fields optional?))

(defn registration-form []
  (let [fields (atom {})]
    (fn []
      [c/modal
       [:div "Picture Gallery Registeration"]
       [:div
        [:div.well.well-sm
         [:strong [:span.glyphicon.glyphicon-asterisk]
          "required field"]]
        [text-input "name" :user "enter a user name" fields]
        [email-input "email" :email "enter your email" fields true]
        [password-input "password" :pass "enter a password" fields]
        [password-input "password" :pass-confirm "re-enter the password" fields]]
       [:div
        [:button.btn.btn-danger
         {:on-click #(session/remove! :modal)}
         "Cancel"]
        [:button.btn.btn-primary
        {:on-click #(register! fields)}
        "Register"]]])))
