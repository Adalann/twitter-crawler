# twitter-crawler [School Project]
Tweets capture, storage and analysis

This project is a school projet meant to learn and practice concepts of Operating System such as multithreading or network.
The goal is to create a Java application able to connect on Twitter, capture tweets and apply the HIST algorithm on these data.

We'll use the Twitter4J library, licensed under the Apache License, Version 2.0, Copyright 2007 Yusuke Yamamoto without any modification. (http://twitter4j.org/en/index.html)
We are using the GSON library too, licensed under the Apache License, Version 2.0, Copyright 2008 Google Inc without any modification. (https://github.com/google/gson)


## Getting started
The firt step to use this program is to create a twitter application (https://apps.twitter.com/). Then you need the oauth informations related to your application (https://developer.twitter.com/en/docs/basics/authentication/guides/single-user).

The next step is to run for the first time the startCrawler script. It will create a bin directory and will ask you your authentication informations in order to create a configuration file for the Twitter4J API. It will create a crawlerConf.json file too. This is a default configuration file for the crawler module, you can change the settings as you want.
At this point the crawler is working and you just have to run the startAnalyser and startIndexor scripts.
