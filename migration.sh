ENV=$1
if [ $ENV == '' ]
then 
echo 'Bitte zu startende Umgebung angeben [development, production]'
exit 0
fi
java -Xms256m -Xmx512m -Dspring.config.location=./config/application.properties -Dspring.profiles.active=%ENV% -jar ./lib/usermanager-0.0.1-SNAPSHOT.jar migration