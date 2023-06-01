package ru.kyamshanov.mission.project.missionproject.dto

import ru.kyamshanov.mission.project.missionproject.models.Participant

data class ParticipantDto(
    val userId: String,
    val userLogin: String,
    val role: Role
) {

    enum class Role {
        PARTICIPANT, LEADER, MENTOR
    }
}

internal fun Participant.toDto() = ParticipantDto(
    userId = userInfo.userId,
    userLogin = userInfo.userName,
    role = role.toDto()
)

internal fun ParticipantDto.Role.toDomain(): Participant.Role = when (this) {
    ParticipantDto.Role.PARTICIPANT -> Participant.Role.PARTICIPANT
    ParticipantDto.Role.LEADER -> Participant.Role.LEADER
    ParticipantDto.Role.MENTOR -> Participant.Role.MENTOR
}

internal fun Participant.Role.toDto() = when (this) {
    Participant.Role.PARTICIPANT -> ParticipantDto.Role.PARTICIPANT
    Participant.Role.LEADER -> ParticipantDto.Role.LEADER
    Participant.Role.MENTOR -> ParticipantDto.Role.MENTOR
}