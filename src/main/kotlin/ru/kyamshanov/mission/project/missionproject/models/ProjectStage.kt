package ru.kyamshanov.mission.project.missionproject.models

import java.time.LocalDateTime

data class ProjectStage(
    val stage: Stage,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {

    enum class Stage {

        CREATED, CANCELED, PREPARING, RESEARCH, DEVELOP, FINISHING
    }
}

