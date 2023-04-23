package ru.kyamshanov.mission.project.missionproject.models

import java.time.LocalDateTime

data class ShortTaskModel(
    val id: String,
    val title: String,
    val description: String,
    val taskStage: TaskStage,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val points: Int?,
    val maxPoints: Int
)
