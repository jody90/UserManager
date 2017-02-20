CREATE TABLE IF NOT EXISTS users (
  id int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  username varchar(45) NOT NULL,
  password varchar(45) NOT NULL,
  lastname varchar(45) NOT NULL,
  firstname varchar(45) NOT NULL,
  email varchar(45) NOT NULL
);

CREATE TABLE IF NOT EXISTS roles (
  id int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  name varchar(120) UNIQUE NOT NULL,
  description LONGTEXT NULL
);

CREATE TABLE IF NOT EXISTS rights (
  id int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  name varchar(120) UNIQUE NOT NULL,
  description LONGTEXT NULL
);
