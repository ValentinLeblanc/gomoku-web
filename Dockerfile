FROM openjdk:21-jdk
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} gomoku-web.jar
COPY wait-for-it.sh /usr/local/bin/wait-for-it.sh
ENTRYPOINT ["java","-jar","/gomoku-web.jar"]