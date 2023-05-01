package ru.kyamshanov.mission.project.missionproject.dto

import ru.kyamshanov.mission.project.missionproject.models.ProjectRestrictedModel
import ru.kyamshanov.mission.project.missionproject.models.TaskStage

data class AttachedProjectsInfoDto(
    val projectId: String,
    val title: String,
    val state: ProjectStageDto,
)

fun ProjectRestrictedModel.toDto() = AttachedProjectsInfoDto(
    projectId = projectId,
    title = title,
    state = stage.toProjectStageDto()
)

fun TaskStage.toProjectStageDto() = when (this) {
    TaskStage.WAIT -> ProjectStageDto.WAIT
    TaskStage.IN_PROGRESS -> ProjectStageDto.IN_PROGRESS
    TaskStage.FINISHED -> ProjectStageDto.FINISHED
}