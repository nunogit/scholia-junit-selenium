#!/bin/bash

/usr/bin/Xvfb &
/usr/local/bin/chromedriver &

#sh -e /etc/init.d/xvfb start

mvn test
