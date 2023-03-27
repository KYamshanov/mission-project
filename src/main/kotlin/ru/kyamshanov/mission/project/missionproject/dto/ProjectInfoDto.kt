package ru.kyamshanov.mission.project.missionproject.dto

data class ProjectInfoDto(
    val id: String,
    val title: String,
    val description: String,
    val tasks: List<ShortTaskDto>
)

