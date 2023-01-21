/**
  Скрипт для создания/пересоздания новых таблиц
 */
DROP TABLE IF EXISTS projects CASCADE;
DROP TABLE IF EXISTS participants CASCADE;
DROP TYPE IF EXISTS participants_role;

CREATE TABLE projects
(
    id          VARCHAR(50) PRIMARY KEY,
    title       VARCHAR(50) NOT NULL,
    description TEXT        NOT NULL
);

CREATE TYPE participants_role AS ENUM ('LEADER');

CREATE TABLE participants
(
    id               VARCHAR(50) PRIMARY KEY,
    project_id       VARCHAR(50)       NOT NULL REFERENCES projects (id),
    external_user_id VARCHAR(50)       NOT NULL,
    participant_role participants_role NULL DEFAULT null
)