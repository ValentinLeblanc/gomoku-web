FROM openjdk:21-jdk
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} gomoku-web.jar
ENTRYPOINT ["java","-jar","/gomoku-web.jar"]