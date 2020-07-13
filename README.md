# Scholia jUnit Selenium tests

> A set of classes to test several aspects in Scholia performance

> scholia, junit, selenium, performance

---

## Table of Contents (Optional)

> If your `README` has a lot of info, section headers might be nice.

- [Install and run](#install and run)
- [Features](#features)
- [Contributing](#contributing)
- [Team](#team)
- [FAQ](#faq)
- [Support](#support)
- [License](#license)


---


## Install and run

### Clone

- Clone this repo to your local machine using `https://github.com/nunogit/scholia-junit-selenium`

### Setup

- You can build this software using maven, however we suggest using docker directly as it simplifies the process of building and running

> Setting up with Docker

Dockerfile is made in a multistaged process. The first stage will create the environmet to build the java jar, the second stage will generate the environment to run the jar. Since chromedriver is being used for Selenium the enviroment will already be preared with it and a virtual frame buffer (Xvfb).

```shell
$ docker build --no-cache -t scholia/websitevalidation . 
```

This will create an image named scholia/websitevalidation.
--no-cache will guarantee that the image will be built from scratch. If you want to speed up the image creation you can remove the option.

---

## Features

This peace of software can be used directly to report errors in Jarvis or Jenkins. However is will only report failed or passed tests, representing failed widgets (data tables or SPARQL Iframes).

If used as standalone this software can report:
 As a performance check:
 - performance statistics for prometheus
 - performance reports, exported to GIT
 Other functionality 
 - automatically collect Sparql queries from Scholia pages
 - Run the queries on a cache server to speed up Scholia operation with a caching proxy


## Usage

### Example using docker

```shell
$ docker run -e SCHOLIA_CONFIG=/home/scholiatest/config-test.properties -v /home/scholia/etc/junit-websitevalidation/:/home/scholiatest/  -p 1234:1234 scholia/websitevalidation
```

 -e SCHOLIA_CONFIG environment variable set inside docker pointing to the confiuration file
 -v /home/scholia/etc/junit-websitevalidation/:/home/scholiatest/
 -p 1234:1234 port exporting data ready for prometheus ingestion

## Documentation - configuration file



## License

[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)

- **[MIT license](http://opensource.org/licenses/mit-license.php)**
- Copyright 2015 © <a href="http://fvcproductions.com" target="_blank">FVCproductions</a>.
