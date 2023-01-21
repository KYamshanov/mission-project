package ru.kyamshanov.mission.project.missionproject.dto

data class CreateProjectRqDto(
    val title: String,
    val description: String
)

data class CreateProjectRsDto(
    val id: String,
    val title: String,
    val description: String
)

