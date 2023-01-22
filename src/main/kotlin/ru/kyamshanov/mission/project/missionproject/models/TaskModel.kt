package ru.kyamshanov.mission.project.missionproject.models

import java.time.LocalDateTime

data class TaskModel(
    val projectId: String,
    val title: String,
    val text: String,
    val createAt: LocalDateTime = LocalDateTime.now(),
    val id: String? = null
)
