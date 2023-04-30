package ru.kyamshanov.mission.project.missionproject.service

import kotlinx.coroutines.flow.toCollection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.kyamshanov.mission.project.missionproject.entity.ParticipantEntity
import ru.kyamshanov.mission.project.missionproject.entity.ParticipantRole
import ru.kyamshanov.mission.project.missionproject.entity.toEntity
import ru.kyamshanov.mission.project.missionproject.entity.toModel
import ru.kyamshanov.mission.project.missionproject.models.Participant
import ru.kyamshanov.mission.project.missionproject.models.Team
import ru.kyamshanov.mission.project.missionproject.network.usecase.FetchUsersUseCase
import ru.kyamshanov.mission.project.missionproject.repository.ParticipantCrudRepository

interface TeamService {

    suspend fun attachTeam(projectId: String, team: Team)

    suspend fun getTeam(projectId: String): Team

    suspend fun addParticipant(projectId: String, participant: Participant)

    suspend fun setLeader(projectId: String, participant: Participant)
    suspend fun setMentor(projectId: String, participant: Participant)
    suspend fun setRole(projectId: String, participant: Participant, role: Participant.Role)

    suspend fun removeParticipant(projectId: String, participant: Participant)
}

@Service
class TeamServiceImpl @Autowired constructor(
    private val participantCrudRepository: ParticipantCrudRepository,
    private val fetchUsersUseCase: FetchUsersUseCase
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
            .map { it.toModel() }
            .let { Team(it) }
            .let { fetchUsersUseCase.fetchUserNames(it).getOrNull() ?: it }

    @Transactional
    override suspend fun addParticipant(projectId: String, participant: Participant) {
        val foundParticipant =
            participantCrudRepository.findFirstByProjectIdAndUserId(projectId, participant.userInfo.userId)
        val savingEntity = ParticipantEntity(
            projectId = projectId,
            userId = participant.userInfo.userId,
            role = participant.role.toEntity(),
            givenId = foundParticipant?.id
        )
        participantCrudRepository.save(savingEntity)
    }

    @Transactional
    override suspend fun setLeader(projectId: String, participant: Participant) {
        participantCrudRepository.findAllByProjectIdAndRole(projectId, ParticipantRole.LEADER)
            .toCollection(mutableListOf())
            .map { it.copy(role = null) }
            .also {
                participantCrudRepository.saveAll(it).toCollection(mutableListOf())
                    .also { saved -> assert(it.size == saved.size) { "Not all entities were saved" } }
            }
        participantCrudRepository.setRole(
            projectId = projectId,
            userId = participant.userInfo.userId,
            role = ParticipantRole.LEADER
        ).toCollection(mutableListOf())
            .also { assert(it.size == 1) { "Entity was updated with exception" } }
    }

    @Transactional
    override suspend fun setMentor(projectId: String, participant: Participant) {
        participantCrudRepository.findAllByProjectIdAndRole(projectId, ParticipantRole.MENTOR)
            .toCollection(mutableListOf())
            .map { it.copy(role = null) }
            .also {
                participantCrudRepository.saveAll(it).toCollection(mutableListOf())
                    .also { saved -> assert(it.size == saved.size) { "Not all entities were saved" } }
            }
        participantCrudRepository.setRole(
            projectId = projectId,
            userId = participant.userInfo.userId,
            role = ParticipantRole.MENTOR
        ).toCollection(mutableListOf())
            .also {
                println(it)
                assert(it.size == 1)
                { "Entity was updated with exception" }
            }
    }

    override suspend fun setRole(projectId: String, participant: Participant, role: Participant.Role) {
        participantCrudRepository.setRole(
            projectId = projectId,
            userId = participant.userInfo.userId,
            role = role.toEntity()
        ).toCollection(mutableListOf())
            .also { assert(it.size == 1) { "Entity was updated with exception" } }
    }

    override suspend fun removeParticipant(projectId: String, participant: Participant) {
        participantCrudRepository.removeAllByProjectIdAndUserId(
            projectId = projectId,
            userId = participant.userInfo.userId
        )
    }
}