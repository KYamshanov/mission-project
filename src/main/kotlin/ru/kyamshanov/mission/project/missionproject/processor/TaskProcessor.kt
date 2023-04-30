package ru.kyamshanov.mission.project.missionproject.processor

import org.springframework.stereotype.Component
import ru.kyamshanov.mission.project.missionproject.dto.GetTaskRsDto
import ru.kyamshanov.mission.project.missionproject.dto.toDto
import ru.kyamshanov.mission.project.missionproject.models.UserId
import ru.kyamshanov.mission.project.missionproject.service.AvailabilityService
import ru.kyamshanov.mission.project.missionproject.service.TaskService

interface TaskProcessor {

    suspend fun getTask(requester: UserId, taskId: String): GetTaskRsDto
}

@Component
class TaskProcessorImpl(
    private val taskService: TaskService,
    private val availabilityService: AvailabilityService,
) : TaskProcessor {
    override suspend fun getTask(requester: UserId, taskId: String): GetTaskRsDto =
        taskService.getTask(taskId)
            .let { taskModel ->
                val availableCreationSubtask =
                    availabilityService.availableCreationSubtask(projectId = taskModel.projectId, userId = requester)
                GetTaskRsDto(
                    title = taskModel.title,
                    text = taskModel.text,
                    createdAt = taskModel.createAt,
                    taskStage = taskModel.stage.toDto(),
                    startAt = taskModel.startAt,
                    endAt = taskModel.endAt,
                    maxPoints = taskModel.maxPoints,
                    points = taskModel.points,
                    id = requireNotNull(taskModel.id),
                    availableAddSubtask = availableCreationSubtask,
                )
            }
}