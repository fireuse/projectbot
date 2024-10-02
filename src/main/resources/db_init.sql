CREATE DATABASE IF NOT EXISTS bot;

USE bot;

CREATE TABLE IF NOT EXISTS projects(
    id INT NOT NULL auto_increment,
    creator BIGINT,
    name VARCHAR(200),
    description VARCHAR(4000),
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS allowed_roles(
  role BIGINT
);

