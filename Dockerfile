FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} gomoku-web.jar
ENTRYPOINT ["java","-jar","/gomoku-web.jar"]