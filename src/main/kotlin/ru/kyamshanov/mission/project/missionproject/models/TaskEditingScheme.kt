package ru.kyamshanov.mission.project.missionproject.models

data class TaskEditingScheme(
    val titleEdited: Boolean,
    val descriptionEdited: Boolean,
    val startAtEdited: Boolean,
    val endAtEdited: Boolean,
    val pointsEdited: Boolean,
    val maxPointsEdited: Boolean,
)
