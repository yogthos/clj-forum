(ns clj-forum.components.post
  (:require [reagent.core :refer [atom]]
            [reagent.session :as session]
            [clojure.string :as string]
            [ajax.core :as ajax]))

(defn post-component [{:keys [preview title url author comments timestamp]}]
  [:div
   [:h3 (when preview [:img {:url preview}]) [:a {:href url} title]]
   [:p (count comments) " comments"]])
