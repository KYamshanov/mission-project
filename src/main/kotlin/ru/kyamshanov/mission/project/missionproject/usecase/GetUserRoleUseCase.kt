package ru.kyamshanov.mission.project.missionproject.usecase

import kotlinx.coroutines.flow.toCollection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.kyamshanov.mission.project.missionproject.entity.toModel
import ru.kyamshanov.mission.project.missionproject.models.Participant
import ru.kyamshanov.mission.project.missionproject.models.ShortTaskModel
import ru.kyamshanov.mission.project.missionproject.models.TimeRestriction
import ru.kyamshanov.mission.project.missionproject.models.UserId
import ru.kyamshanov.mission.project.missionproject.repository.ParticipantCrudRepository

interface GetUserRoleUseCase {

    suspend operator fun invoke(userId: UserId, projectId: String): Participant.Role
}

@Component
class GetUserRoleUseCaseImpl @Autowired constructor(
    private val participantCrudRepository: ParticipantCrudRepository
) : GetUserRoleUseCase {
    override suspend fun invoke(userId: UserId, projectId: String): Participant.Role =
        participantCrudRepository.findAllByProjectIdAndUserId(projectId, userId).toCollection(mutableListOf())
            .findLast { it.role != null }?.role.toModel()
}