package ru.kyamshanov.mission.project.missionproject.dto

data class AttachTeamRqDto(
    val project: String,
    val participants: Collection<String>
)