#
# Package stage
#
FROM openjdk:latest
COPY ./target/gomoku-0.0.1-SNAPSHOT.jar /usr/local/lib/gomoku.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/gomoku.jar"]