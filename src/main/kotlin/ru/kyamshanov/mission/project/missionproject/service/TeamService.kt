package ru.kyamshanov.mission.project.missionproject.service

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toCollection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.kyamshanov.mission.project.missionproject.entity.ParticipantEntity
import ru.kyamshanov.mission.project.missionproject.entity.toEntity
import ru.kyamshanov.mission.project.missionproject.entity.toModel
import ru.kyamshanov.mission.project.missionproject.models.Participant
import ru.kyamshanov.mission.project.missionproject.models.Team
import ru.kyamshanov.mission.project.missionproject.repository.ParticipantCrudRepository

interface TeamService {

    suspend fun attachTeam(projectId: String, team: Team)

    suspend fun getTeam(projectId: String): Team

    suspend fun addParticipant(projectId: String, participant: Participant)
}

@Service
class TeamServiceImpl @Autowired constructor(
    private val participantCrudRepository: ParticipantCrudRepository
) : TeamService {

    @Transactional
    override suspend fun attachTeam(projectId: String, team: Team) {
        val teamEntity = team.participants.map { user -> user.toEntity(projectId) }
        participantCrudRepository.removeAllByProjectId(projectId)
        val savedEntityFlow = participantCrudRepository.saveAll(teamEntity)
        assert(savedEntityFlow.toCollection(mutableListOf()).size == team.participants.size) { "Error saving team." }
    }

    override suspend fun getTeam(projectId: String): Team =
        participantCrudRepository.findAllByProjectId(projectId).toCollection(mutableListOf())
            .map { it.toModel() }.let { Team(it) }

    @Transactional
    override suspend fun addParticipant(projectId: String, participant: Participant) {
        val foundParticipant =
            participantCrudRepository.findFirstByProjectIdAndUserId(projectId, participant.userId).firstOrNull()
        val savingEntity = ParticipantEntity(
            projectId = projectId,
            userId = participant.userId,
            role = participant.role.toEntity(),
            givenId = foundParticipant?.id
        )
        participantCrudRepository.save(savingEntity)
    }
}