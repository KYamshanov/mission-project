package ru.kyamshanov.mission.project.missionproject.service

import kotlinx.coroutines.flow.toCollection
import org.springframework.stereotype.Service
import ru.kyamshanov.mission.project.missionproject.entity.ParticipantRole
import ru.kyamshanov.mission.project.missionproject.entity.SubTaskEntity
import ru.kyamshanov.mission.project.missionproject.models.UserId
import ru.kyamshanov.mission.project.missionproject.repository.ParticipantCrudRepository
import ru.kyamshanov.mission.project.missionproject.repository.SubTaskCrudRepository

internal interface AvailabilityService {

    suspend fun availableCreationSubtask(userId: UserId, projectId: String): Boolean

    suspend fun availableEditSubtask(userId: UserId, projectId: String): Boolean

    suspend fun availableSetExecutionResult(userId: UserId, subtask: SubTaskEntity): Boolean
}

@Service
private class AvailabilityServiceImpl(
    private val participantCrudRepository: ParticipantCrudRepository,
    private val subtaskCrudRepository: SubTaskCrudRepository
) : AvailabilityService {
    override suspend fun availableCreationSubtask(userId: UserId, projectId: String): Boolean {
        val roles = participantCrudRepository.findAllByProjectIdAndUserId(projectId = projectId, userId = userId)
            .toCollection(mutableListOf()).mapNotNull { it.role }
        return roles.contains(ParticipantRole.LEADER)
    }

    override suspend fun availableEditSubtask(userId: UserId, projectId: String): Boolean =
        availableCreationSubtask(userId, projectId)

    override suspend fun availableSetExecutionResult(userId: UserId, subtask: SubTaskEntity): Boolean {
        return subtask.responsible == userId
    }

}