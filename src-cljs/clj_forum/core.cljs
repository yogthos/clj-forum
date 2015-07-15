(ns clj-forum.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [clojure.string :as string]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]]
            [clj-forum.components.post :as post]
            [clj-forum.components.common :refer [error-modal]]
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
     [:h2 "Welcome to Clojure Forum"]
     [post/post-list]]
    [:div.col-sm-4
     [sidebar]]]])

(def pages
  {:home #'home-page
   :about #'about-page
   :post #'post/post-page})

(def modals
  {:registration #'registration-form
   :submission #'submission-form})

(defn page []
  (println "page:" (str (session/get :page)))
  [:div
   [error-modal]
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

(secretary/defroute "/post/:id" [id]
  (println "loading post page...")
  (post/load-post-page! id)
  (session/put! :page :post))

(defn lower-case [string]
  (when string
    (string/lower-case string)))

(defn str-contains? [string substr]
  (when (and string substr)
    (> (.indexOf (lower-case string) (lower-case substr)) -1)))

#_(defn redirect-to-page []
  (let [[_ uri] (string/split (.-URL js/document) #"\#")]
    (session/put!
      :page
      (cond
        (str-contains? uri "/post") :post
        :else :home))
    (secretary/dispatch! (or uri "/"))))

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
(defn mount-components []
  (reagent/render-component [#'navbar] (.getElementById js/document "navbar"))
  (reagent/render-component [#'page] (.getElementById js/document "app")))

(defn init! []
  (hook-browser-navigation!)
  (mount-components))


