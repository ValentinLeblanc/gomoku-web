#!/bin/bash
cd ./src/main/nginx
docker build -t gomoku-engine-http:1.0.0 .
cd ../../../