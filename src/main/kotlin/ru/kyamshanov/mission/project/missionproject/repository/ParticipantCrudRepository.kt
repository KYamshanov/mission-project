package ru.kyamshanov.mission.project.missionproject.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import ru.kyamshanov.mission.project.missionproject.entity.ParticipantEntity

interface ParticipantCrudRepository : CoroutineCrudRepository<ParticipantEntity, String> {
    suspend fun removeAllByProjectId(projectId: String)

    fun findAllByProjectId(projectId: String): Flow<ParticipantEntity>

    fun findFirstByProjectIdAndUserId(projectId: String, userId: String): Flow<ParticipantEntity>
}