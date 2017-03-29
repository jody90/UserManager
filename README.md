# Requirements
* Java 8
* Maven apache-maven-3.3.9
* MariaDB mariadb-10.1.21-winx64

# Development

## Compile

	mvn clean install
	
## Run
Use the profile "development".

### Start with Maven

	mvn spring-boot:run -Dspring.profiles.active=development

### System Properties to use if you want to use the JMX console 

-Dcom.sun.management.jmxremote
-Dcom.sun.management.jmxremote.port=9888
-Dcom.sun.management.jmxremote.ssl=FALSE
-Dcom.sun.management.jmxremote.authenticate=FALSE

## Database
On development we use a H2 database. The h2 database can be accessed over the webconsole (/h2-console). On production environment the webconsole is forbidden.

	spring.datasource.url=jdbc:h2:file:./data/application;DB_CLOSE_ON_EXIT=FALSE
	spring.datasource.username=
	spring.datasource.password=
	spring.datasource.driver-class-name=org.h2.Driver
	spring.h2.console.path=/h2-console
	spring.h2.console.enabled=true

New database objects will be created by liquibase automatically. On production the database migration has to be started manually.

To deactivate the automatically database migration use the application.property:

	liquibase.enabled=false
	
If you deactivate the database migration in the development envirnonment you have to manually update the database object using:

	migration.bat <environmnet>

# Production
User following steps to create a productive artifact.

## Create Artifact

	mvn release:prepare
	mvn release:perform -Darguments="-Dmaven.deploy.skip=true"

### Further documentation
For test purpose:

	mvn release:prepare -DdryRun=true

To prepare the release

	mvn release:prepare
	
To create the release

	mvn release:perform -Darguments="-Dmaven.deploy.skip=true"
	
Cleanup

	mvn release:clean
	
Reset a Version
	
	mvn versions:set -DnewVersion=1.0.1-SNAPSHOT

## Install new Version
Be aware that the application-production.properties are updated in the source code.
The application-production.properties containing the current production configuration.

### Replace
1. Make a Backup of the old installation directory
2. Replace the old installation with the new installation

### Update Database
Use the profile "production" (-Dspring.profiles.active=production)

	migration.bat production

### Run
Use the profile "production" (-Dspring.profiles.active=production)

	start.bat production

# Versioning
Simple Versioning Strategy (see also http://kylelieber.com/2012/06/maven-versioning-strategy/).
Especially the production artifact is the same as the tested RC artifact.
- 1.0.0-SNAPSHOT (Development Version)
- 1.0.1 (RC1 Test Version)
- 1.0.2 (RC2 Test Version)
- 1.0.3 (RC3 Test Version) (Test is OK)
- 1.0.3 (Final Version) (Same artifact as 1.0.3)
- 1.1.0-SNAPSHOT (Next Development Version)
- 1.1.1 (RC1 Test Version)
- 1.0.4 (RC4 Test Version for a production Bugfix)
- 1.0.5 (RC5 Test Version for a production Bugfix) (Test is OK)
- 1.0.5 (Final Version for a production Bugfix) (Same artifact as 1.0.5)

# API documentation
## Rest

	<server>:<port>/swagger-ui.html

# Command Line

## Git

Client side configuration for the git user

	git config --global user.mail "anendroes@sortimo.de"
	git config --global user.name "Andreas Endrös"

Change the user of the repository

	git remote set-url origin http://endroes@localhost:8080/r/Comcenter.git
	
## Maven

Create a BRANCH from a TAG.

1. Checkout the TAG

	svn co svn://svn/SST/ComManager/tags/<tag> ComManager.<tag>

2. Change into the newly created directory

3. Create the BRANCH

	mvn release:branch -DbranchName=<BRANCH> -DautoVersionSubmodules=true -DsuppressCommitBeforeBranch=true -DremoteTagging=false -DupdateBranchVersions=true -DupdateWorkingCopyVersions=false

You will be asked about the new version for the BRANCH

4. Delete the TAG from the local filesystem and checkout the newly created BRANCH.
Because a TAG is a copy of the original (no pointer) you can commit into the SVN TAG too.

	svn co svn://svn/SST/ComManager/branches/<branch> ComManager.<branch>

# Documentation

## Start/Stop Script
Create script for start|stop|restart|status: https://dzone.com/articles/managing-spring-boot

## Scheduled Task
If you have more than one node of the main application disable the tasks in the main application (using an appropriate cron expression).
Create another configuration profile only for the tasks and disable the tasks in the main application. The profile is used to start the task execution on a separate node. Scalable task execution is not supported. See https://github.com/lukas-krecan/ShedLock#start-of-content for
more information.

## Queues/Topics
An ActiveMQ is used and started with the application.
