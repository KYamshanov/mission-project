package ru.kyamshanov.mission.project.missionproject.dto

import ru.kyamshanov.mission.project.missionproject.models.UserId
import java.time.LocalDateTime

data class EditSubtaskRqDto(
    val subtaskId: String,
    val title: String?,
    val description: String?,
    val responsible: UserId?,
    val state: SubTaskStageDto?,
    val startAt: LocalDateTime?,
    val endAt: LocalDateTime?,
    val executionResult: String?,
)


