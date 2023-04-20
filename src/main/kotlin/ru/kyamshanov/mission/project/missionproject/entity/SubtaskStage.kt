package ru.kyamshanov.mission.project.missionproject.entity

import ru.kyamshanov.mission.project.missionproject.models.SubtaskModel

enum class SubtaskStage {
    CREATED,
    IN_WORK,
    FINISHED
}

fun SubtaskStage.toModel() = when (this) {
    SubtaskStage.CREATED -> SubtaskModel.Stage.CREATED
    SubtaskStage.IN_WORK -> SubtaskModel.Stage.IN_WORK
    SubtaskStage.FINISHED -> SubtaskModel.Stage.FINISHED
}

fun SubtaskModel.Stage.toEntity() = when (this) {
    SubtaskModel.Stage.CREATED -> SubtaskStage.CREATED
    SubtaskModel.Stage.IN_WORK -> SubtaskStage.IN_WORK
    SubtaskModel.Stage.FINISHED -> SubtaskStage.FINISHED
}