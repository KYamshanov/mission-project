package ru.kyamshanov.mission.project.missionproject.models

data class ShortTaskModel(
    val id: String,
    val title: String,
    val description: String,
    val taskStage: TaskStage
)
