package ru.kyamshanov.mission.project.missionproject.dto

import com.fasterxml.jackson.annotation.JsonFormat
import ru.kyamshanov.mission.project.missionproject.models.ShortTaskModel
import java.time.LocalDateTime

data class ShortTaskDto(
    val id: String,
    val title: String,
    val description: String,
    val taskStage: TaskStageDto,
    @JsonFormat(pattern = "YYYY-MM-dd'T'hh:mm:ss")
    val startAt: LocalDateTime,
    @JsonFormat(pattern = "YYYY-MM-dd'T'hh:mm:ss")
    val endAt: LocalDateTime,
    val points: Int?
)

fun ShortTaskModel.toDto() = ShortTaskDto(
    id = id,
    title = title,
    description = description,
    taskStage = taskStage.toDto(),
    startAt = startAt,
    endAt = endAt,
    points = points
)