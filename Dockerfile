#
# Package stage
#
FROM openjdk:latest
COPY ./target/gomoku-web-0.0.1-SNAPSHOT.jar /usr/local/lib/gomoku-web.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/gomoku-web.jar"]