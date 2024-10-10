CREATE TABLE IF NOT EXISTS projects
(
    id          INT NOT NULL auto_increment,
    creator     BIGINT,
    name        VARCHAR(200),
    description VARCHAR(4000),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS allowed_roles
(
    role BIGINT,
    projectId INT,
    FOREIGN KEY (projectId) REFERENCES projects(id)
);

