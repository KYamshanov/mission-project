package ru.kyamshanov.mission.project.missionproject.dto

import ru.kyamshanov.mission.project.missionproject.models.SubtaskModel
import java.time.LocalDateTime

data class SubtaskDto(
    val taskId: String,
    val title: String,
    val description: String,
    val createAt: LocalDateTime = LocalDateTime.now(),
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val responsible: UserInfoDto,
    val stage: SubTaskStageDto,
    val executionResult: String?,
    val id: String,
)

enum class SubTaskStageDto {
    CREATED,
    IN_WORK,
    FINISHED
}

fun SubtaskModel.Stage.toDto(): SubTaskStageDto = when (this) {
    SubtaskModel.Stage.CREATED -> SubTaskStageDto.CREATED
    SubtaskModel.Stage.IN_WORK -> SubTaskStageDto.IN_WORK
    SubtaskModel.Stage.FINISHED -> SubTaskStageDto.FINISHED
}

fun SubtaskModel.toDto() = SubtaskDto(
    taskId = taskId,
    title = title,
    description = description,
    createAt = createAt,
    startAt = startAt,
    endAt = endAt,
    responsible = responsible.toDto(),
    stage = stage.toDto(),
    executionResult = executionResult,
    id = requireNotNull(id) { "Id cannot be null for DTO" }
)