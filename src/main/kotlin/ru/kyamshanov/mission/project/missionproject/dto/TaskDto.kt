package ru.kyamshanov.mission.project.missionproject.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class CreateTaskRqDto(
    val projectId: String,
    val title: String,
    val text: String
)

data class CreateTaskRsDto(
    val taskId: String,
    @JsonFormat(pattern = "YYYY-MM-DD'T'hh:mm:ss")
    val createdAt: LocalDateTime
)

data class GetTaskRsDto(
    val title: String,
    val text : String,
    @JsonFormat(pattern = "YYYY-MM-DD'T'hh:mm:ss")
    val createdAt: LocalDateTime
)

data class GetTasksRsDto(
    val tasks : Collection<String>
)