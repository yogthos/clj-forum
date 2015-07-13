(ns clj-forum.components.post-preview)

(defn preview-component [{:keys [preview title url author comments timestamp]}]
  [:div
   [:h3 (when preview [:img {:url preview}]) [:a {:href url} title]]
   [:p [:span  "submitted by " author " at " timestamp ]]
   [:p (count comments) " comments"]])
