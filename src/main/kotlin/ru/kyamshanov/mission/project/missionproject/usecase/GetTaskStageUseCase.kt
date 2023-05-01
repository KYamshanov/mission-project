package ru.kyamshanov.mission.project.missionproject.usecase

import org.springframework.stereotype.Component
import ru.kyamshanov.mission.project.missionproject.models.*
import java.time.LocalDateTime

interface GetTaskStageUseCase {

    operator fun invoke(startAt: LocalDateTime, endAt: LocalDateTime): TaskStage
}

@Component
class GetTaskStageUseCaseImpl() : GetTaskStageUseCase{
    override fun invoke(startAt: LocalDateTime, endAt: LocalDateTime): TaskStage {
        val nowTime = LocalDateTime.now()
        return when {
            nowTime < startAt -> TaskStage.WAIT
            nowTime > endAt -> TaskStage.FINISHED
            else -> TaskStage.IN_PROGRESS
        }
    }

}