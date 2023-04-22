package ru.kyamshanov.mission.project.missionproject.dto

import java.time.LocalDateTime

data class EditTaskRqDto(
    val taskId: String,
    val title: String?,
    val description: String?,
    val startAt: LocalDateTime?,
    val endAt: LocalDateTime?,
    val maxPoints: Int?,
    val points: Int?
)


