package ru.kyamshanov.mission.project.missionproject.dto

import com.fasterxml.jackson.annotation.JsonFormat
import ru.kyamshanov.mission.project.missionproject.models.TaskModel
import java.time.LocalDateTime
import java.util.Date

data class CreateTaskRqDto(
    val projectId: String,
    val title: String,
    val text: String,
    @JsonFormat(pattern = "YYYY-MM-DD'T'hh:mm:ss")
    val startAt: Date,
    @JsonFormat(pattern = "YYYY-MM-DD'T'hh:mm:ss")
    val endAt: Date,
    val maxPoints: Int
)

data class CreateTaskRsDto(
    val taskId: String
)

data class GetTaskRsDto(
    val title: String,
    val text: String,
    @JsonFormat(pattern = "YYYY-MM-DD'T'hh:mm:ss")
    val createdAt: LocalDateTime,
    val taskStage: TaskStageDto,
    @JsonFormat(pattern = "YYYY-MM-DD'T'hh:mm:ss")
    val startAt: LocalDateTime,
    @JsonFormat(pattern = "YYYY-MM-DD'T'hh:mm:ss")
    val endAt: LocalDateTime,
    val maxPoints: Int,
    val points: Int
)

data class GetTasksRsDto(
    val tasks: Collection<String>
)