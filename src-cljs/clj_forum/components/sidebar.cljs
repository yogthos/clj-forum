(ns clj-forum.components.sidebar
  (:require [reagent.core :refer [atom]]
            [reagent.session :as session]
            [clj-forum.components.login :as login]))

(defn sidebar []
  [:div
   (if (session/get :identity)
     [login/logout-component]
     [:div
      [login/login-component]
      [:p "or"]
      [:a {:on-click #(session/put! :modal :registration)}
       "register"]])])
