(ns website.core-test
  (:require [clojure.test :refer :all]
            [website.core :refer :all]))

(deftest test-get-pages
  (testing "Check get-pages returns something stasis can work with."
    (is (map? (get-pages)))))
