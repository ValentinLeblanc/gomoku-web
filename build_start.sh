#!/bin/bash
cd ../gomoku-engine
mvn clean install -DskipTests
cd ../gomoku-web
mvn clean install -DskipTests
docker-compose up -d
docker-compose logs -f