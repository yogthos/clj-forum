(ns clj-forum.app
  (:require [clj-forum.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
