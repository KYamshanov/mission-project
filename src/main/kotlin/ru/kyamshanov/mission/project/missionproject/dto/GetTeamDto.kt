package ru.kyamshanov.mission.project.missionproject.dto

import ru.kyamshanov.mission.project.missionproject.models.Participant

data class GetTeamRsDto(
    val project: String,
    val participants: Collection<Participant>
)