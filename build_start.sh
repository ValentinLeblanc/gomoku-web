#!/bin/bash
cd ../gomoku-engine
mvn clean install -DskipTests
cd ../gomoku-web/src/main/nginx
docker build -t gomoku-engine-http:1.0.0 .
cd ../../../
mvn clean install -DskipTests
docker-compose up -d
docker-compose logs -f