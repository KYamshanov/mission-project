package ru.kyamshanov.mission.project.missionproject.dto

import ru.kyamshanov.mission.project.missionproject.models.Participant

data class SetRoleRqDto(
    val projectId: String,
    val userId: String,
    val role : Participant.Role
)