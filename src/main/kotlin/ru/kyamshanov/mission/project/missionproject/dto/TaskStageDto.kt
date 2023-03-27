package ru.kyamshanov.mission.project.missionproject.dto

import ru.kyamshanov.mission.project.missionproject.models.TaskStage

enum class TaskStageDto {
    WAIT,
    IN_PROGRESS,
    FINISHED

}

fun TaskStage.toDto(): TaskStageDto = when (this) {
    TaskStage.WAIT -> TaskStageDto.WAIT
    TaskStage.IN_PROGRESS -> TaskStageDto.IN_PROGRESS
    TaskStage.FINISHED -> TaskStageDto.FINISHED
}