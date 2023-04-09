package ru.kyamshanov.mission.project.missionproject.usecase

import org.springframework.stereotype.Component
import ru.kyamshanov.mission.project.missionproject.models.ShortTaskModel
import ru.kyamshanov.mission.project.missionproject.models.TaskModel
import ru.kyamshanov.mission.project.missionproject.service.TaskService
import java.time.LocalDateTime

interface GetCurrentTaskUseCase {

    suspend operator fun invoke(tasks: Collection<ShortTaskModel>): TaskModel?
}

@Component
private class GetCurrentTaskUseCaseImpl(
    private val taskService: TaskService
) : GetCurrentTaskUseCase {
    override suspend fun invoke(tasks: Collection<ShortTaskModel>): TaskModel? {
        val nowTime = LocalDateTime.now()
        return tasks.find { it.startAt <= nowTime && nowTime <= it.endAt }
            ?.let { taskService.getTask(it.id) }
    }


}
