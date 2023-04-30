package ru.kyamshanov.mission.project.missionproject.models

data class SubtaskEditingScheme(
    val titleEdited: Boolean,
    val descriptionEdited: Boolean,
    val responsibleEdited: Boolean,
    val stateEdited: Boolean,
    val executionResultEdited: Boolean,
    val startAtEdited: Boolean,
    val endAtEdited: Boolean
)

val SubtaskEditingScheme.editOnlyExecutionResult
    get() = executionResultEdited and titleEdited.not() and descriptionEdited.not() and responsibleEdited.not() and stateEdited.not()