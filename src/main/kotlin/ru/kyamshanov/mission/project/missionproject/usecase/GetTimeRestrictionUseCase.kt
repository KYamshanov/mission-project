package ru.kyamshanov.mission.project.missionproject.usecase

import org.springframework.stereotype.Component
import ru.kyamshanov.mission.project.missionproject.models.ShortTaskModel
import ru.kyamshanov.mission.project.missionproject.models.TaskModel
import ru.kyamshanov.mission.project.missionproject.models.TimeRestriction

interface GetTimeRestrictionUseCase {

    operator fun invoke(tasks: Collection<ShortTaskModel>): TimeRestriction
}

@Component
private class GetTimeRestrictionUseCaseImpl : GetTimeRestrictionUseCase {
    override fun invoke(tasks: Collection<ShortTaskModel>): TimeRestriction {
        val endAt = tasks.maxBy { it.endAt }.endAt
        val startAt = tasks.minBy { it.startAt }.startAt
        return TimeRestriction(
            startAt = startAt,
            endAt = endAt
        )
    }


}