package ru.kyamshanov.mission.project.missionproject.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class CreateSubTaskRqDto(
    val taskId: String,
    val title: String,
    val description: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val responsible: String,
)

data class CreateSubTaskRsDto(
    val id: String
)