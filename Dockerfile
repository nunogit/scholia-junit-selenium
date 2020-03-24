# Needs optimization
FROM selenium/standalone-chrome

USER root

#ENV TMP_DIR /tmp
#WORKDIR $TMP_DIR

RUN apt-get update -y
#RUN apt-get install -y curl
RUN apt-get install -y xvfb
#RUN apt-get install -y chromium
RUN ls
RUN wget -N http://chromedriver.storage.googleapis.com/2.39/chromedriver_linux64.zip -P ~/
RUN unzip ~/chromedriver_linux64.zip -d ~/
RUN rm ~/chromedriver_linux64.zip

RUN mv -f ~/chromedriver /usr/local/share/
RUN chmod +x /usr/local/share/chromedriver
RUN ln -s /usr/local/share/chromedriver /usr/local/bin/chromedriver

RUN apt-get install -y git
RUN apt-get install -y maven
RUN apt-get install -y openjdk-8-jdk
#RUN /usr/local/bin/chromedriver
#RUN sh -e /etc/init.d/xvfb start

RuN mkdir /log/
# caching dependencies - this only runs if pom.xml changes
RUN mkdir scholia
RUN git clone https://github.com/nunogit/scholia-junit-selenium.git ./scholia
WORKDIR scholia/

COPY startProcesses.sh .
RUN chmod +x startProcesses.sh

ENV DRIVER /usr/local/bin/chromedriver
ENV CHROME_BIN /usr/bin/google-chrome
ENV DISPLAY ':99.0'


CMD ./startProcesses.sh
