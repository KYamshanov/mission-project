package ru.kyamshanov.mission.project.missionproject.dto

data class FindProjectRsDto(
    val id: String,
    val title: String,
    val description: String,
    val participants: List<ParticipantDto>,
    val tasks: List<ShortTaskDto>
)