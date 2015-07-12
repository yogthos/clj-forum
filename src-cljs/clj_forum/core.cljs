(ns clj-forum.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]]
            [clj-forum.components.sidebar :refer [sidebar]]
            [clj-forum.components.registration :refer [registration-form]]
            [clj-forum.components.submission :refer [submission-form]])
  (:import goog.History))

(session/put! :identity "foo")

(defn navbar []
  [:div.navbar.navbar-inverse.navbar-fixed-top
   [:div.container
    [:div.navbar-header
     [:a.navbar-brand {:href "#/"} "myapp"]]
    [:div.navbar-collapse.collapse
     [:ul.nav.navbar-nav
      [:li {:class (when (= :home (session/get :page)) "active")}
       [:a {:href "#/"} "Home"]]
      [:li {:class (when (= :about (session/get :page)) "active")}
       [:a {:href "#/about"} "About"]]]]]])

(defn about-page []
  [:div "this is the story of clj-forum... work in progress"])

(defn home-page []
  [:div.container
   [:div.row
    [:div.col-md-8
     [:h2 "Welcome to Clojure Forum"]]
    [:div.col-sm-4
     [sidebar]]]])

(def pages
  {:home #'home-page
   :about #'about-page})

(def modals
  {:registration #'registration-form
   :submission #'submission-form})

(defn page []
  [:div
   (if-let [modal (session/get :modal)]
     [(modals modal)])
   [(pages (session/get :page))]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :page :home))

(secretary/defroute "/about" []
  (session/put! :page :about))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
        (events/listen
          EventType/NAVIGATE
          (fn [event]
              (secretary/dispatch! (.-token event))))
        (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn fetch-docs! []
      (GET "/docs" {:handler #(session/put! :docs %)}))

(defn mount-components []
  (reagent/render-component [#'navbar] (.getElementById js/document "navbar"))
  (reagent/render-component [#'page] (.getElementById js/document "app")))

(defn init! []
  (fetch-docs!)
  (hook-browser-navigation!)
  (session/put! :page :home)
  (mount-components))


