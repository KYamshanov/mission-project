package ru.kyamshanov.mission.project.missionproject.dto

import ru.kyamshanov.mission.project.missionproject.models.ProjectModel

data class ShortProjectInfoDto(
    val id: String,
    val title: String,
    val description: String,
)

fun ProjectModel.toShortProjectInfoDto() = ShortProjectInfoDto(
    id = requireNotNull(id),
    title = title,
    description = description
)

