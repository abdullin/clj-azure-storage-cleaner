(ns azure-clean.core
  (:import [com.microsoft.azure.storage CloudStorageAccount]
           [com.microsoft.azure.storage.blob CloudBlobClient CloudBlobContainer CloudBlockBlob CloudBlob])
  (:require [clj-time.core :as t]
            [clj-time.coerce :as c])
  (:gen-class))

(defn conn-str
  [name key]
  (str "DefaultEndpointsProtocol=http;"
         "AccountName=" name
         ";AccountKey=" key
         ";"))

(defn blob-client [conn] (.createCloudBlobClient (CloudStorageAccount/parse conn)))
(defn old? [blob period]
  (let [
        date (c/from-date (.getLastModified (.getProperties blob)))
        limit (t/minus (t/now) period)
        ] (t/before? date limit)))

(defn clean
  [conn-str container period]
  (let [blobs (.listBlobs (.getContainerReference (blob-client conn-str) container) "" true)]
    (doseq [blob (filter #(old? % period) blobs)]
      (println (str "Deleting old blob " (.getName blob)))
      (.delete blob)
      )))

(defn -main
    [name key folder & args]
  (clean (conn-str name key) folder (t/months 1)))
