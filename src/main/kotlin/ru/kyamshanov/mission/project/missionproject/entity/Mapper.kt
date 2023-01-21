package ru.kyamshanov.mission.project.missionproject.entity

import ru.kyamshanov.mission.project.missionproject.models.ProjectModel

fun ProjectModel.toEntity() = ProjectEntity(
    title = title,
    description = description,
    givenId = id
)

fun ProjectEntity.toModel() = ProjectModel(
    title = title,
    description = description,
    id = id
)
