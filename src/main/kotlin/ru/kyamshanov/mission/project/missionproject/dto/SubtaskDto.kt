package ru.kyamshanov.mission.project.missionproject.dto

import com.fasterxml.jackson.annotation.JsonFormat
import ru.kyamshanov.mission.project.missionproject.models.SubtaskModel
import java.time.LocalDateTime

data class SubtaskDto(
    val taskId: String,
    val title: String,
    val description: String,
    @JsonFormat(pattern = "YYYY-MM-dd'T'hh:mm:ss")
    val createAt: LocalDateTime = LocalDateTime.now(),
    @JsonFormat(pattern = "YYYY-MM-dd'T'hh:mm:ss")
    val startAt: LocalDateTime,
    @JsonFormat(pattern = "YYYY-MM-dd'T'hh:mm:ss")
    val endAt: LocalDateTime,
    val responsible: UserInfoDto,
    val stage: SubTaskStageDto,
    val executionResult: String?,
    val id: String,
    val availableEdit: Boolean,
    val availableSetResult: Boolean,
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

fun SubTaskStageDto.toDomain(): SubtaskModel.Stage = when (this) {
    SubTaskStageDto.CREATED -> SubtaskModel.Stage.CREATED
    SubTaskStageDto.IN_WORK -> SubtaskModel.Stage.IN_WORK
    SubTaskStageDto.FINISHED -> SubtaskModel.Stage.FINISHED
}

fun SubtaskModel.toDto(
    availableEdit: Boolean,
    availableSetResult: Boolean,
) = SubtaskDto(
    taskId = taskId,
    title = title,
    description = description,
    createAt = createAt,
    startAt = startAt,
    endAt = endAt,
    responsible = responsible.toDto(),
    stage = stage.toDto(),
    executionResult = executionResult,
    id = requireNotNull(id) { "Id cannot be null for DTO" },
    availableEdit = availableEdit,
    availableSetResult = availableSetResult,
)