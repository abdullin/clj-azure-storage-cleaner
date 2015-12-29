# azure_clean

A simple azure cleaner tool written in clojure. It will recursively
delete all files older than one month in the specified folder. Launch
it via jar or lein with:

    $ lein run "account" "storage" "folder"
