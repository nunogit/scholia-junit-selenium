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

---

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

**pagetestset** URL where the Scholia page list is located. This resource should be a simple text file. Each line should contain a valid Scholia URL. It accepts comments starting with an hash.

**id** this is the test set running id. This is used on reports to identify which running instance generated the data

**webpageTimout** maximum allowed time for a test to run in seconds

**timeBetweenTests** the test batch runs continuously. Every time the batch end it restarts again producing results. A time between tests can be specified (in seconds) if there is the need to delay them

Tests can be exported to Github. In this case one needs to provide login credential (its highly advised that these credentials should be unique to this task and not belonging a user)

**github.username** user username
**github.password** user password
**github.repository** repository where the performance results will be stored


### prometheus variables explained

Scholia jUnit Selenium tests exports data to prometheus, to be rendered in Grafana.

The following variables are currently being exported:

**Backendpeformance**

Time to start receiving data from the server. This includes server processing time and latency.
This values should be low by default since there is almost no server side processing. 
This is measured in milliseconds.

**Frontendperformance**

Frontend performance measures the time the browser takes to render the HTML content received from the server. 
This values don't include any async calls that might request extra data (eg.:Datatables) to render content.
This is measured in milliseconds.

**Widget Iframe loading time**

The iframe loading time measures the time that a Sparql Widget (iframe based) takes to render.
Since these are some of the most complex parts of the pages processing (remote query + rendering time) this can often take several seconds and it's very prone to failure.
Assessing when an iframe is fully loaded is not trivial due to the asynchronous nature of the process. This is currently being done by parsing the iframe page for a successful end result.
This probing is done every second. This time granularity seems to be enough for now.
This metric is measured in seconds.

**Page loading time**

Page loading time is extremely difficult to get since the content is composed of several asynchronous calls. Some of them run in parallel. A good method to evaluate this still needs to be established. For now we can look at the individual results of each page component to assess its performance.
Loaded/Failed DataTables
This metric counts the number of falied/rendered data tables.
This metric is rather poor since we test a limited set of pages (the most problematic / most used ones). Despite this it can give valuable information when comparing different setups of scholia. It allows a quick comparison to find if different architectural / performance approaches give better results 

**Loaded/Failed Iframe Widgets**

This metric counts the number of failed/rendered iframe widgets.
This metric is rather poor since we test a limited set of pages (the most problematic / most used ones). Despite this it can give valuable information when comparing different setups of scholia. //It allows a quic


---

## License

[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)

- **[MIT license](http://opensource.org/licenses/mit-license.php)**
