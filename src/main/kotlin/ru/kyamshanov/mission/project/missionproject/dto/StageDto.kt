package ru.kyamshanov.mission.project.missionproject.dto

import com.fasterxml.jackson.annotation.JsonFormat
import ru.kyamshanov.mission.project.missionproject.models.ProjectStage
import java.time.LocalDateTime

data class SetStageRqDto(
    val projectId: String,
    val stage: ProjectStage.Stage
)

data class HistoryRsDto(
    val history: List<ProjectStageDto>
)

data class ProjectStageRsDto(
    val stage: ProjectStageDto
)

data class ProjectStageDto(
    val stage: ProjectStage.Stage,
    @JsonFormat(pattern = "YYYY-MM-DD'T'hh:mm:ss")
    val createdAt: LocalDateTime
)

/* ---- Маппер ----*/

fun ProjectStage.toDto() = ProjectStageDto(
    stage = stage,
    createdAt = createdAt
)