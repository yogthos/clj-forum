(ns clj-forum.components.post
  (:require [reagent.core :refer [atom]]
            [reagent.session :as session]
            [clojure.string :as string]
            [clj-forum.ajax :as ajax]
            [clj-forum.components.common :as c]
            [clj-forum.components.sidebar :refer [sidebar]]))

(def post (atom nil))

(def posts
  [{:preview   nil
    :id   0
    :title     "OMFG!"
    :url       "http://google.com"
    :author    "yogthos"
    :comments  []
    :timestamp (js/Date.)}
   {:preview   "https://b.thumbs.redditmedia.com/T0_7o5d-4odKDMszwu3YWd-uVYZqTO1SF3OALupOdbM.jpg"
    :id   1
    :title     "MORE OMFG!"
    :url       "http://google.com"
    :author    "yogthos"
    :comments  []
    :timestamp (js/Date.)}])

(defn post-page []
  (fn []
    (println "hello?")
    (if-let [{:keys [preview title url author comments timestamp]} @post]
      [:div.container
       [:div.row
        [:div.col-md-8
         [:div.row
          [:div.col-md-12
           [:h2 title]]
          [:div.col-md-12
           [:h3 "comments"]]]]
        [:div.col-sm-4
         [sidebar]]]]
      c/spinner)))

(defn post-preview-component [{:keys [preview title url id author comments timestamp]}]
  [:div
   [:h3 (when preview [:img {:url preview}]) [:a {:href url} title]]
   [:a {:href (str "#/post/" id)} [:span (count comments) " comments"]]])

(defn post-list []
  [:div.row
   [:div.col-md-12
    (for [post posts]
      ^{:key (:id post)}
      [post-preview-component post])]])

(defn load-post-page! [id]
  (ajax/GET "/api/post"
            {:params  {:id id}
             :handler #(reset! post %)}))