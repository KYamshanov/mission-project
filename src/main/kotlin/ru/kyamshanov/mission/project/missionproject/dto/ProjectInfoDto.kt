package ru.kyamshanov.mission.project.missionproject.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class ProjectInfoDto(
    val id: String,
    val title: String,
    val description: String,
    val currentTask: GetTaskRsDto?,
    @JsonFormat(pattern = "YYYY-MM-dd'T'hh:mm:ss")
    val startAt: LocalDateTime,
    @JsonFormat(pattern = "YYYY-MM-dd'T'hh:mm:ss")
    val endAt: LocalDateTime,
    val stage: ProjectStageDto,
    val tasks: List<ShortTaskDto>
)

