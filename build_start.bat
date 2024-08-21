call build_http.bat
call build_engine.bat
cd ..\gomoku-web && mvn clean install -DskipTests && docker-compose up -d && docker-compose logs -f
