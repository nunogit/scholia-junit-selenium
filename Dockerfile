# Needs optimization
FROM maven:3.6.3-jdk-8

RUN apt-get update -y
RUN mkdir /scholia
RUN git clone https://github.com/nunogit/scholia-junit-selenium.git /scholia
WORKDIR /scholia/
#RUN echo $M2_HOME
#RUN echo $M3_HOME
RUN mvn -Dmaven.test.skip=true package


#ENV TMP_DIR /tmp
#WORKDIR $TMP_DIR

FROM selenium/standalone-chrome

USER root

COPY --from=0 /scholia/target/java-junit-selenium-1.0-SNAPSHOT-jar-with-dependencies.jar .

RUN apt-get update -y
#RUN apt-get install -y curl
RUN apt-get install -y xvfb
#RUN apt-get install -y chromium

RUN wget -N http://chromedriver.storage.googleapis.com/2.39/chromedriver_linux64.zip -P ~/
RUN unzip ~/chromedriver_linux64.zip -d ~/
RUN rm ~/chromedriver_linux64.zip

RUN mv -f ~/chromedriver /usr/local/share/
RUN chmod +x /usr/local/share/chromedriver
RUN ln -s /usr/local/share/chromedriver /usr/local/bin/chromedriver

#COPY startProcesses.sh .
#RUN chmod +x startProcesses.sh

ENV DRIVER /usr/local/bin/chromedriver
ENV CHROME_BIN /usr/bin/google-chrome
ENV DISPLAY ':99.0'
ENV SCHOLIA_CONFIG /scholia/config-test.properties


CMD /usr/bin/Xvfb &

EXPOSE 1234

CMD java -cp java-junit-selenium-1.0-SNAPSHOT-jar-with-dependencies.jar opendata.scholia.prometheus.exporter.HttpExporter
