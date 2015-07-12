(ns clj-forum.components.common
  (:require [reagent.core :refer [atom]]
            [reagent.session :as session]))

(def modal-backdrop
  [:div.modal-backdrop.fade.in])

(defn modal [header body footer]
  [:div
   [:div.modal-dialog
    [:div.modal-content
     [:div.modal-header [:h3 header]]
     [:div.modal-body body]
     [:div.modal-footer
      [:div.bootstrap-dialog-footer
       footer]]]]
   modal-backdrop])

(defn error-modal []
  (when-let [error (session/get :error)]
    [modal
     (:message error)
     [:div.container-fluid
      [:div.form-group
       [:div.alert.alert-danger (:cause error)]]]
     [:div.form-group
      [:button.btn.btn-primary.btn-lg.btn-block
       {:type     "submit"
        :on-click #(session/remove! :error)}
       "OK"]]]))

(defn input [type opts id placeholder fields]
  [(if (= :textarea type)
     :textarea.form-control.input-lg
     :input.form-control.input-lg)
   (merge
     {:placeholder placeholder
      :value       (id @fields)
      :on-change   #(swap! fields assoc id (-> % .-target .-value))}
     (when-not (= :textarea type)
       {:type        type})
     opts)])

(defn form-input [type opts label id placeholder fields optional?]
  [:div.form-group
   [:label label]
   (if optional?
     [input type opts id placeholder fields]
     [:div.input-group
      [input type opts id placeholder fields]
      [:span.input-group-addon
       [:span.glyphicon.glyphicon-asterisk]]])])

(defn text-input [label id placeholder fields & [optional?]]
  (form-input :text {} label id placeholder fields optional?))

(defn textarea [rows label id placeholder fields & [optional?]]
  (form-input :textarea {:rows (or rows 5)} label id placeholder fields optional?))

(defn email-input [label id placeholder fields & [optional?]]
  (form-input :email {} label id placeholder fields optional?))

(defn password-input [label id placeholder fields & [optional?]]
  (form-input :password {} label id placeholder fields optional?))