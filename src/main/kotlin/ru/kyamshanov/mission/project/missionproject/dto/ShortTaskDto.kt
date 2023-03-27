package ru.kyamshanov.mission.project.missionproject.dto

import ru.kyamshanov.mission.project.missionproject.models.ShortTaskModel

data class ShortTaskDto(
    val id: String,
    val title: String,
    val description: String,
    val taskStage: TaskStageDto
)

fun ShortTaskModel.toDto() = ShortTaskDto(
    id = id,
    title = title,
    description = description,
    taskStage = taskStage.toDto(),
)