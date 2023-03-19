package ru.kyamshanov.mission.project.missionproject.dto

import ru.kyamshanov.mission.project.missionproject.models.ProjectModel

data class ProjectInfoDto(
    val id: String,
    val title: String,
    val description: String
)

fun ProjectModel.toProjectInfoDto() = ProjectInfoDto(
    id = requireNotNull(id) { "Project id cannot be null" },
    title = title,
    description = description
)

