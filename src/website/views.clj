(ns website.views
  (:gen-class)
  (:require [clojure.string :as str]
            [clj-time.core :as t]
            [hiccup.page :refer [html5 include-css include-js]]))

(defn head [context]
  [:head
   (when-let [redirect (:redirect context)]
     [:meta {:http-equiv "refresh"
             :content (str "0; url=" redirect)}])
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1, shrink-to-fit=no"}]
   [:title (str (:name context) "'s website.")]
   [:base {:href "/"}]
   (include-css "/bundle.css")
   (include-js "/bundle.js")])

(defn header [context]
  [:div {:class "row align-items-center"}])

(defn footer [context]
  [:div {:class "row align-items-center"}
   [:div {:class "col"}
    (str "&copy; 2018 - " (t/year (t/now)) " " (:name context))]])

(defmulti main-content :view-name :default :not-found)

(defmethod main-content :generic [context]
  [:article
   [:h1 (:title context)]
   (:content context)])

(defmethod main-content :post [context]
  [:article
   [:h1 (:title context)]
   (:content context)])

(defmethod main-content :not-found [context]
  [:h1 "Not found"])

(defn main [context]
  [:div {:class "row"}
   [:div {:class "profile-meta"}
    [:ul {:class "list-group list-group-flush"}
     [:li {:class "list-group-item"}
      [:a {:href "/" :title (:name context)}
       [:img {:src (:image context)}]]]
     [:li {:class "list-group-item"}
      [:span
       [:a {:href (:email context) :title "Email"}
        [:i {:class "fa fa-at"}] " Email"]]]
     [:li {:class "list-group-item"}
      [:span
       [:a {:href (:twitter context) :title "Twitter"}
        [:i {:class "fa fa-twitter"}] " Twitter"]]]
     [:li {:class "list-group-item"}
      [:span
       [:a {:href (:github context) :title "GitHub"}
        [:i {:class "fa fa-github"}] " GitHub"]]]
     [:li {:class "list-group-item"}
      [:span
       [:a {:href (:keybase context) :title "Keybase"}
        [:i {:class "fa fa-key"}] " Keybase"]]]]]
   [:div {:class "profile-content"}
    (main-content context)]])

(defn layout [context]
  (html5
   {:lang "en"}
   (head context)
   [:body
    [:div {:class "horizon"} [:header {:class "container"} (header context)]]
    [:section#main {:class "container"} (main context)]
    [:footer {:class "container"} (footer context)]]))
