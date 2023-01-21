package ru.kyamshanov.mission.project.missionproject.service

import kotlinx.coroutines.flow.toCollection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.kyamshanov.mission.project.missionproject.entity.ParticipantEntity
import ru.kyamshanov.mission.project.missionproject.models.Team
import ru.kyamshanov.mission.project.missionproject.repository.ParticipantCrudRepository

interface TeamService {

    suspend fun attachTeam(projectId: String, team: Team)

    suspend fun getTeam(projectId: String): Team
}

@Service
class TeamServiceImpl @Autowired constructor(
    private val participantCrudRepository: ParticipantCrudRepository
) : TeamService {

    @Transactional
    override suspend fun attachTeam(projectId: String, team: Team) {
        val teamEntity = team.participants.map { userId -> ParticipantEntity(projectId, userId) }
        participantCrudRepository.removeAllByProjectId(projectId)
        val savedEntityFlow = participantCrudRepository.saveAll(teamEntity)
        assert(savedEntityFlow.toCollection(mutableListOf()).size == team.participants.size) { "Error saving team." }
    }

    override suspend fun getTeam(projectId: String): Team =
        participantCrudRepository.findAllByProjectId(projectId).toCollection(mutableListOf())
            .map { it.userId }.let { Team(it) }
}