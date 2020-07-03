# twitter-crawler [School Project]
Tweets capture, storage and analysis

This project is a school projet meant to learn and practice concepts of Operating System such as multithreading and networking.
The goal is to create a Java application able to connect on Twitter, capture tweets and apply the HIST algorithm on the extracted data.

## Getting started
The first step to use this program is to create a twitter application (https://apps.twitter.com/). Then you need the oauth informations related to your application (https://developer.twitter.com/en/docs/basics/authentication/guides/single-user).

The next step is to run for the first time the startCrawler script. It will create a bin directory and will ask you your authentication informations in order to create a configuration file for the Twitter4J API. It will create a crawlerConf.json file too. This is a default configuration file for the crawler module, you can change the settings as you want.
At this point the crawler is working and you just have to run the startAnalyser and startIndexor scripts.

## Licence

We are using for this project the Twitter4J library, licensed under the Apache License, Version 2.0, Copyright 2007 Yusuke Yamamoto without any modification (http://twitter4j.org/en/index.html) and the GSON library too, licensed under the Apache License, Version 2.0, Copyright 2008 Google Inc without any modification (https://github.com/google/gson).

This program is published under the [Apache 2.0 license](LICENSE).

```
Copyright 2018 Théo Martos and Jules Perret.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
