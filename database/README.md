# Installation
Following SQL has to be executed in order to install the application appropriate on an external database (no h2).
Attention: Use the % appropriate to configure the access to the database.

	CREATE DATABASE IF NOT EXISTS usermanager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci
	;
	
	CREATE USER 'dba'@'%' IDENTIFIED BY 'sortimo';
	GRANT ALL PRIVILEGES ON usermanager.* TO 'dba'@'%' WITH GRANT OPTION;
	
	CREATE USER 'dbu'@'%' IDENTIFIED BY 'sortimo';
	GRANT INSERT ON usermanager.* TO 'dbu'@'%' WITH GRANT OPTION;
	GRANT UPDATE ON usermanager.* TO 'dbu'@'%' WITH GRANT OPTION;
	GRANT DELETE ON usermanager.* TO 'dbu'@'%' WITH GRANT OPTION;
	GRANT SELECT ON usermanager.* TO 'dbu'@'%' WITH GRANT OPTION;

# Migration

1. Check the property file if the database user and administration user is correct.

2. To update the database use following command:

	migration.bat
