(ns clj-forum.components.sidebar
  (:require [reagent.core :refer [atom]]
            [reagent.session :as session]
            [clj-forum.components.login :as login]
            [clj-forum.components.submission :as sub]))

(defn sidebar []
  [:div
   (if (session/get :identity)
     [:div
      [login/logout-component]
      [sub/submit-post]]
     [:div
      [login/login-component]
      [:p "or"]
      [:a {:on-click #(session/put! :modal :registration)}
       "register"]])])
