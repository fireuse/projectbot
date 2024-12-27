CREATE TABLE IF NOT EXISTS projects
(
    id          INT NOT NULL auto_increment,
    creator     BIGINT,
    category    BIGINT,
    name        VARCHAR(200),
    description VARCHAR(4000),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS allowed_roles
(
    role      BIGINT,
    projectId INT,
    FOREIGN KEY (projectId) REFERENCES projects (id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS faq_categories
(
    id   INT NOT NULL auto_increment,
    name VARCHAR(50),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS faq
(
    id       INT NOT NULL auto_increment,
    question VARCHAR(200),
    answer   VARCHAR(200),
    category INT,
    FOREIGN KEY (category) REFERENCES faq_categories (id)
        ON DELETE CASCADE,
    PRIMARY KEY (id)
);

INSERT INTO faq_categories (name) SELECT 'Ważne' WHERE NOT EXISTS(SELECT (name) FROM faq_categories WHERE name='Ważne')

