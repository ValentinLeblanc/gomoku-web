call mvn clean install -DskipTests
call docker-compose up -d
call docker-compose logs -f