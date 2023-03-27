package ru.kyamshanov.mission.project.missionproject.models

import java.time.LocalDateTime

data class TaskModel(
    val projectId: String,
    val title: String,
    val text: String,
    val createAt: LocalDateTime = LocalDateTime.now(),
    val stage: TaskStage,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val maxPaints: Int,
    val points: Int = -1,
    val id: String? = null
)
