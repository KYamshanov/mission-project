package ru.kyamshanov.mission.project.missionproject.dto


data class GetTeamRsDto(
    val project: String,
    val participants: Collection<ParticipantDto>
)