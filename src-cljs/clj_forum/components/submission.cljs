(ns clj-forum.components.submission
  (:require [reagent.core :refer [atom]]
            [reagent.session :as session]
            [clj-forum.components.common :as c]))

(defn submit! [fields])

(defn submission-form []
  (let [fields (atom {})]
    (fn []
      [c/modal
       [:div "Submit a story"]
       [:div
        [:div.well.well-sm
         [:strong [:span.glyphicon.glyphicon-asterisk]
          "required field"]]
        [c/text-input "title" :title "enter the title for the post" fields]
        [c/text-input "link" :link "URL of the post" fields true]
        [:p "or"]
        [c/textarea 5 "description" :description "description" fields true]]
       [:div
        [:button.btn.btn-danger
         {:on-click #(session/remove! :modal)}
         "Cancel"]
        [:button.btn.btn-primary
         {:on-click #(submit! fields)}
         "Submit"]]])))

(defn submit-post []
  [:button.btn.btn-primary
   {:on-click #(session/put! :modal :submission)}
   "submit a story"])
