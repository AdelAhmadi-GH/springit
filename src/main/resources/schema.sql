-- This file is used to define the database schema for the Spring Boot application.
-- It includes the creation of tables and their relationships.
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS link;

-- Table link
-- This table stores links with their metadata such as creation date, last modified date, created by, last modified by, title, and URL.
-- The primary key is the id, which is auto-incremented.
CREATE TABLE link (
  link_id BIGINT NOT NULL AUTO_INCREMENT,
  created_by VARCHAR(255) DEFAULT NULL,
  creation_date DATETIME DEFAULT NULL,
  last_modified_by VARCHAR(255) DEFAULT NULL,
  last_modified_date DATETIME DEFAULT NULL,
  title VARCHAR(255) DEFAULT NULL,
  url VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (link_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table comment
-- This table stores comments related to links. It includes fields for the comment body, creation date, last modified date, created by, last modified by, and a foreign key link_id that references the link table.
-- The primary key is the id, which is auto-incremented. The foreign key constraint ensures that the link_id corresponds to an existing id in the link table.
CREATE TABLE comment (
  comment_id BIGINT NOT NULL AUTO_INCREMENT,
  created_by VARCHAR(255) DEFAULT NULL,
  creation_date DATETIME DEFAULT NULL,
  last_modified_by VARCHAR(255) DEFAULT NULL,
  last_modified_date DATETIME DEFAULT NULL,
  body VARCHAR(255) DEFAULT NULL,
  link_id BIGINT DEFAULT NULL,
  PRIMARY KEY (comment_id),
  KEY idx_comment_link_id (link_id),
  CONSTRAINT fk_comment_link
    FOREIGN KEY (link_id) REFERENCES link(link_id)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
