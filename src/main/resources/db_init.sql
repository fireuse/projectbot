CREATE DATABASE IF NOT EXISTS bot;

USE bot;

CREATE TABLE IF NOT EXISTS projects(
    id INT NOT NULL auto_increment,
    creator BIGINT,
    name VARCHAR(200),
    PRIMARY KEY(id)
);

