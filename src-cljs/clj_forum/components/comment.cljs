(ns clj-forum.components.comment
  (:require [clj-forum.components.common :as c]
            [reagent.session :as session]
            [ajax.core :as ajax]))

(defn submit-comment! [comment]
  (ajax/POST "/comment"
             {:params @comment
              :handler #(do
                         ;;TODO update displayed comments
                         (reset! comment {}))
              :error-handler #(session/put! :error {:message "failed to submit the comment"
                                                     :cause "unknown"})}))

(defn comment-editor []
  (let [comment (atom {})]
    [:div
     [c/input :textarea {:rows 5} :comment nil comment]
     [:button.btn.btn-primary {:on-click #()}]]))