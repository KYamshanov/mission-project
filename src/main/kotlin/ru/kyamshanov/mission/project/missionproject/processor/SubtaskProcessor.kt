package ru.kyamshanov.mission.project.missionproject.processor

import org.springframework.stereotype.Component
import ru.kyamshanov.mission.project.missionproject.dto.GetSubTaskRsDto
import ru.kyamshanov.mission.project.missionproject.dto.GetTaskRsDto
import ru.kyamshanov.mission.project.missionproject.dto.SubtaskDto
import ru.kyamshanov.mission.project.missionproject.dto.toDto
import ru.kyamshanov.mission.project.missionproject.models.UserId
import ru.kyamshanov.mission.project.missionproject.service.AvailabilityService
import ru.kyamshanov.mission.project.missionproject.service.SubtaskService
import ru.kyamshanov.mission.project.missionproject.service.TaskService

interface SubtaskProcessor {

    suspend fun getSubtasks(requester: UserId, taskId: String): GetSubTaskRsDto

    suspend fun getSubtask(requester: UserId, subtaskId: String): SubtaskDto
}

@Component
class SubtaskProcessorImpl(
    private val subtaskService: SubtaskService,
    private val availabilityService: AvailabilityService,
) : SubtaskProcessor {

    override suspend fun getSubtasks(requester: UserId, taskId: String): GetSubTaskRsDto {
        val subtasksList = subtaskService.getSubTasks(taskId)
        return GetSubTaskRsDto(subtasksList.map {
            val availableEditing = availabilityService.availableEditSubtask(requester, it.taskId)
            val availableSetResult =
                availabilityService.availableSetExecutionResult(userId = requester, subtaskId = requireNotNull(it.id))
            it.toDto(availableEdit = availableEditing, availableSetResult = availableSetResult)
        })
    }

    override suspend fun getSubtask(requester: UserId, subtaskId: String): SubtaskDto {
        return subtaskService.getSubtask(subtaskId).let {
            val availableEditing = availabilityService.availableEditSubtask(requester, it.taskId)
            val availableSetResult =
                availabilityService.availableSetExecutionResult(userId = requester, subtaskId = requireNotNull(it.id))
            it.toDto(availableEdit = availableEditing, availableSetResult = availableSetResult)
        }
    }

}