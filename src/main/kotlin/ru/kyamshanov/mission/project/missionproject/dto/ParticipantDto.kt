package ru.kyamshanov.mission.project.missionproject.dto

import ru.kyamshanov.mission.project.missionproject.models.Participant

data class ParticipantDto(
    val userId: String,
    val userLogin: String,
    val role: Role
) {

    enum class Role {
        PARTICIPANT, LEADER
    }
}

internal fun Participant.toDto() = ParticipantDto(
    userId = userId,
    userLogin = userName,
    role = role.toDto()
)

private fun Participant.Role.toDto() = when (this) {
    Participant.Role.PARTICIPANT -> ParticipantDto.Role.PARTICIPANT
    Participant.Role.LEADER -> ParticipantDto.Role.LEADER
}