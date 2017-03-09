CREATE TABLE IF NOT EXISTS users (
  id int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  username varchar(45) NOT NULL,
  password varchar(255) NOT NULL,
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

CREATE TABLE IF NOT EXISTS users_rights (
  username varchar(45) NOT NULL,
  right_id int(11) NOT NULL,
  PRIMARY KEY (username, right_id)
);

CREATE TABLE IF NOT EXISTS users_roles (
  username varchar(45) NOT NULL,
  role_id int(11) NOT NULL,
  PRIMARY KEY (username, role_id)
);

CREATE TABLE IF NOT EXISTS roles_rights (
  role_id int(11) NOT NULL,
  right_id int(11) NOT NULL,
  PRIMARY KEY (role_id, right_id)
);

INSERT INTO rights (id, name, description) VALUES(1, "superRight", "Wer dieses Recht hat, ist der König der Welt.") ON DUPLICATE KEY UPDATE id=1, name="superRight", description="Wer dieses Recht hat, ist der König der Welt.";
INSERT INTO roles (id, name, description) VALUES(1, "superAdmin", "Gottgleiches Wesen") ON DUPLICATE KEY UPDATE id=1, name="superAdmin", description="Gottgleiches Wesen";
INSERT INTO roles_rights (role_id, right_id) VALUES(1, 1) ON DUPLICATE KEY UPDATE role_id=1, right_id=1;
INSERT INTO users (id, username, password, lastname, firstname, email) VALUES(1, "superadmin", "geheim", "admin", "super", "super@admin.com") ON DUPLICATE KEY UPDATE id=1, username="superadmin", password="geheim", lastname="admin", firstname="super", email="super@admin.com";
INSERT INTO users_roles(username, role_id) VALUES("superadmin", 1) ON DUPLICATE KEY UPDATE username="superadmin", role_id=1;
