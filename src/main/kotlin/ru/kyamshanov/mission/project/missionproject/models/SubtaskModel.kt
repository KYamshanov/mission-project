package ru.kyamshanov.mission.project.missionproject.models

import java.time.LocalDateTime

data class SubtaskModel(
    val taskId: String,
    val title: String,
    val description: String,
    val createAt: LocalDateTime = LocalDateTime.now(),
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val responsible: UserInfo,
    val stage: Stage,
    val executionResult: String? = null,
    val id: String? = null
) {

    enum class Stage {
        CREATED,
        IN_WORK,
        FINISHED
    }
}
