package ru.kyamshanov.mission.project.missionproject.dto

data class AddParticipantRqDto(
    val projectId: String,
    val userId: String,
    val role : ParticipantDto.Role
)