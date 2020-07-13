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
 As a performance and integrity page validator:
 - performance statistics for prometheus
 - performance reports, exported to GIT
 Other functionality:
 - automatically collect Sparql queries from Scholia pages
 - Run the queries on a cache server to speed up Scholia operation with a caching proxy


## Usage

### Example for performance and integrity page validator using docker

```shell
$ docker run -e SCHOLIA_CONFIG=/home/scholiatest/config-test.properties -v /home/scholia/etc/junit-websitevalidation/:/home/scholiatest/  -p 1234:1234 scholia/websitevalidation
```

 -e SCHOLIA_CONFIG environment variable set inside docker pointing to the confiuration file
 -v /home/scholia/etc/junit-websitevalidation/:/home/scholiatest/
 -p 1234:1234 port exporting data ready for prometheus ingestion

## Documentation

### configuration file for performance and integrity page validator


Example configuration file

```
pagetestset=https://raw.githubusercontent.com/nunogit/scholia-junit-selenium/master/pages/pagetotest.csv
id=maindomain
webpageTimeout=30
timeBetweenTests=0
github.username=
github.password=
github.repository=
```

pagetestset URL where the Scholia page list is located. This resource should be a simple text file. Each line should contain a valid Scholia URL. It accepts comments starting with an hash.
id this is the test set running id. This is used on reports to identify which running instance generated the data
webpageTimout maximum allowed time for a test to run in seconds
timeBetweenTests the test batch runs continuously. Every time the batch end it restarts again producing results. A time between tests can be specified (in seconds) if there is the need to delay them

Tests can be exported to Github. In this case one needs to provide login credential (its highly advised that these credentials should be unique to this task and not belonging a user)

github.username user username
github.password user password
github.repository repository where the performance results will be stored


#### prometheus validator explained



## License

[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)

- **[MIT license](http://opensource.org/licenses/mit-license.php)**
