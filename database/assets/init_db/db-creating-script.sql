/**
  Скрипт для создания/пересоздания новых таблиц
 */
DROP TABLE IF EXISTS projects CASCADE;

CREATE TABLE projects
(
    id          VARCHAR(50) PRIMARY KEY,
    title       VARCHAR(50) NOT NULL,
    description TEXT        NOT NULL
);