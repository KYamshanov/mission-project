/**
  Скрипт для создания/пересоздания новых таблиц
 */
DROP TABLE IF EXISTS projects CASCADE;
DROP TABLE IF EXISTS participants CASCADE;
DROP TABLE IF EXISTS stage_history CASCADE;


DROP TYPE IF EXISTS participants_role;
DROP TYPE IF EXISTS project_stage;

CREATE TABLE projects
(
    id          VARCHAR(50) PRIMARY KEY,
    title       VARCHAR(50) NOT NULL,
    description TEXT        NOT NULL
);

CREATE TYPE participants_role AS ENUM ('LEADER');
CREATE TYPE project_stage AS ENUM ('CREATED', 'CANCELED', 'PREPARING','RESEARCH', 'DEVELOP', 'FINISHING');

CREATE TABLE participants
(
    id               VARCHAR(50) PRIMARY KEY,
    project_id       VARCHAR(50)       NOT NULL REFERENCES projects (id),
    external_user_id VARCHAR(50)       NOT NULL,
    participant_role participants_role NULL DEFAULT null
);

CREATE TABLE stage_history
(
    id         VARCHAR(50) PRIMARY KEY,
    project_id VARCHAR(50)   NOT NULL REFERENCES projects (id),
    stage      project_stage not null,
    updatedAt  TIMESTAMPTZ          not null
)
