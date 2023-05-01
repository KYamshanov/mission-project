package ru.kyamshanov.mission.project.missionproject.models

import java.time.LocalDateTime

data class ProjectRestrictedModel(
    val projectId: String,
    val title: String,
    val description: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val stage: TaskStage
)