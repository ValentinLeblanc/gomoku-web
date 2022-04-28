# Gomoku Web application by  Valentin LEBLANC

## Prerequisites
- Java 14
- Maven 3.8.4
- Docker 20.10.14
- Docker Compose 2.4.1

## How to build and start the web application :

- **Build the Gomoku Engine application**

  - Download the **gomoku-engine** project

  - Navigate to the root folder with a command prompt

  - Execute `mvn clean install`

- **Build and start the Gomoku Web application**

  - Download the **gomoku-web** project

  - Navigate to the root folder with a command prompt

  - For Windows, you can execute the file *build_start.bat*

  - Execute `mvn clean install`

  - Execute `docker-compose up -d`

  - For logs : `execute docker-compose logs -f`


## How to stop the application :

- Navigate to the root folder with a command prompt
- For Windows, you can execute *stop.bat*
- Execute `docker-compose down`