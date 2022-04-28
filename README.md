# Gomoku Web application by  Valentin LEBLANC

## Prerequisites
- Java 14
- Maven 3.8.4
- Docker 20.10.14
- Docker Compose 2.4.1

## Build and Start the application (Windows)
- Navigate to the root folder with a command prompt
- Run the "build_start.bat" file

## Build and Start the application (Linux)
- Navigate to the root folder with a command prompt
- Execute "mvn clean install"
- Execute "docker-compose up -d"
- For logs : execute "docker-compose logs -f"

## Stop the application (Windows)
- Navigate to the root folder with a command prompt
- Run the "stop.bat" file

## Stop the application (Linux)
- Navigate to the root folder with a command prompt
- Execute "docker-compose down"

## How to use the Engine
- Download the gomoku-engine project
- Navigate to the root folder with a command prompt
- Execute "mvn clean install"
- Build and start the gomoku web application