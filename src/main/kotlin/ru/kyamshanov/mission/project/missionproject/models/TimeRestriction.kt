package ru.kyamshanov.mission.project.missionproject.models

import java.time.LocalDateTime

data class TimeRestriction(
    val startAt: LocalDateTime,
    val endAt: LocalDateTime
)
