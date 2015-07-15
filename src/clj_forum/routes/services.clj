(ns clj-forum.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s])
  (:import java.util.Date))

(s/defschema Thingie {:id    Long
                      :hot   Boolean
                      :tag   (s/enum :kikka :kukka)
                      :chief [{:name String
                               :type #{{:id String}}}]})

(s/defschema Comment
  {:id Long
   :author String
   :content String
   :timestamp Date})

(s/defschema Post {:id    Long
                   :author String
                   :tags   [String]
                   :preview (s/maybe String)
                   :title String
                   :url (s/maybe String)
                   :timestamp Date
                   :comments [Comment]})

(defn get-post [id]
  (println "hi")
  {:id 0
   :author "Yogthos"
   :tags []
   :preview nil
   :title "Hello World"
   :url "http://google.com"
   :timestamp (Date.)
   :comments []})

(defapi service-routes
        (ring.swagger.ui/swagger-ui
          "/swagger-ui")
        ;JSON docs available at the /swagger.json route
        (swagger-docs
          {:info {:title "Sample api"}})
        (context* "/api" []
                  :tags ["thingie"]

                  (GET* "/post" [id]
                        :return Post
                        :query-params [id :- Long]
                        :summary "return post with the given id"
                        (ok (get-post id)))

                  (GET* "/plus" []
                        :return Long
                        :query-params [x :- Long, {y :- Long 1}]
                        :summary "x+y with query-parameters. y defaults to 1."
                        (ok (+ x y)))

                  (POST* "/minus" []
                         :return Long
                         :body-params [x :- Long, y :- Long]
                         :summary "x-y with body-parameters."
                         (ok (- x y)))

                  (GET* "/times/:x/:y" []
                        :return Long
                        :path-params [x :- Long, y :- Long]
                        :summary "x*y with path-parameters"
                        (ok (* x y)))

                  (POST* "/divide" []
                         :return Double
                         :form-params [x :- Long, y :- Long]
                         :summary "x/y with form-parameters"
                         (ok (/ x y)))

                  (GET* "/power" []
                        :return Long
                        :header-params [x :- Long, y :- Long]
                        :summary "x^y with header-parameters"
                        (ok (long (Math/pow x y))))

                  (PUT* "/echo" []
                        :return [{:hot Boolean}]
                        :body [body [{:hot Boolean}]]
                        :summary "echoes a vector of anonymous hotties"
                        (ok body))

                  (POST* "/echo" []
                         :return (s/maybe Thingie)
                         :body [thingie (s/maybe Thingie)]
                         :summary "echoes a Thingie from json-body"
                         (ok thingie)))

        (context* "/context" []
                  :tags ["context*"]
                  :summary "summary inherited from context"
                  (context* "/:kikka" []
                            :path-params [kikka :- s/Str]
                            :query-params [kukka :- s/Str]
                            (GET* "/:kakka" []
                                  :path-params [kakka :- s/Str]
                                  (ok {:kikka kikka
                                       :kukka kukka
                                       :kakka kakka})))))
