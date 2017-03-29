echo off
set ENV=%1
IF [%1]==[] (
echo Bitte zu startende Umgebung angeben [development, production]
exit /B
)
java -Xms256m -Xmx512m -Dspring.config.location=./config/application.properties -Dspring.profiles.active=%ENV% -jar ./lib/usermanager-0.0.1-SNAPSHOT.jar migration