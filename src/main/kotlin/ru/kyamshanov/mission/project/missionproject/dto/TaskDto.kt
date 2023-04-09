package ru.kyamshanov.mission.project.missionproject.dto

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.format.annotation.DateTimeFormat
import ru.kyamshanov.mission.project.missionproject.models.TaskModel
import java.time.Instant
import java.time.LocalDateTime
import java.util.Date

data class CreateTaskRqDto(
    val projectId: String,
    val title: String,
    val text: String,
    @DateTimeFormat(pattern = "YYYY-MM-dd'T'hh:mm:ss")
    val startAt: Date,
    @DateTimeFormat(pattern = "YYYY-MM-dd'T'hh:mm:ss")
    val endAt: Date ,
    val maxPoints: Int
)

data class CreateTaskRsDto(
    val taskId: String
)

data class GetTaskRsDto(
    val id: String,
    val title: String,
    val text: String,
    @JsonFormat(pattern = "YYYY-MM-dd'T'hh:mm:ss")
    val createdAt: LocalDateTime,
    val taskStage: TaskStageDto,
    @JsonFormat(pattern = "YYYY-MM-dd'T'hh:mm:ss")
    val startAt: LocalDateTime,
    @JsonFormat(pattern = "YYYY-MM-dd'T'hh:mm:ss")
    val endAt: LocalDateTime,
    val maxPoints: Int,
    val points: Int
)

data class GetTasksRsDto(
    val tasks: Collection<String>
)

fun TaskModel.toDto() = GetTaskRsDto(
    id = requireNotNull(id),
    title = title,
    text = text,
    createdAt = createAt,
    taskStage = stage.toDto(),
    startAt = startAt,
    endAt = endAt,
    maxPoints = maxPoints,
    points = points
)