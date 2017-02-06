CREATE TABLE IF NOT EXISTS user (
  id int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  username varchar(45) NOT NULL,
  password varchar(45) NOT NULL,
  lastname varchar(45) NOT NULL,
  firstname varchar(45) NOT NULL,
  email varchar(45) NOT NULL
);
