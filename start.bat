echo off
set ENV=%1
IF [%1]==[] (
echo Bitte zu startende Umgebung angeben [dev, prod]
exit /B
)
java -Xms256m -Xmx512m -server -Xloggc:./logs/gc.log -verbose:gc -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9888 -Dcom.sun.management.jmxremote.ssl=FALSE -Dcom.sun.management.jmxremote.authenticate=FALSE -Dspring.config.location=./config/application.properties -Dspring.profiles.active=%ENV% -jar ./lib/${project.build.finalName}.jar