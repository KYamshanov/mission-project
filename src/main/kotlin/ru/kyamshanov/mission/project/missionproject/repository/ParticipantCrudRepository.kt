package ru.kyamshanov.mission.project.missionproject.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import ru.kyamshanov.mission.project.missionproject.entity.ParticipantEntity
import ru.kyamshanov.mission.project.missionproject.models.UserId

interface ParticipantCrudRepository : CoroutineCrudRepository<ParticipantEntity, String> {
    suspend fun removeAllByProjectId(projectId: String)

    fun findAllByProjectId(projectId: String): Flow<ParticipantEntity>

    suspend fun findFirstByProjectIdAndUserId(projectId: String, userId: String): ParticipantEntity?

    fun findAllByProjectIdAndUserId(projectId: String, userId: String): Flow<ParticipantEntity>

    @Query("SELECT participants.project_id, participants.external_user_id, participants.participant_role, participants.id FROM tasks JOIN projects ON tasks.project_id = projects.id  JOIN participants ON participants.project_id = projects.id  WHERE participants.external_user_id = :userId AND tasks.id = :taskId")
    fun findAllByTaskIdAndUserId(userId: UserId, taskId : String) : Flow<ParticipantEntity>

    @Query("SELECT participants.project_id, participants.external_user_id, participants.participant_role, participants.id FROM subtasks JOIN tasks ON subtasks.task_id = tasks.id JOIN projects ON tasks.project_id = projects.id JOIN participants ON participants.project_id = projects.id  WHERE participants.external_user_id = :userId AND subtasks.id = :subtaskId")
    fun findAllBySubtaskIdAndUserId(userId: UserId, subtaskId : String) : Flow<ParticipantEntity>
}