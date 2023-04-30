package ru.kyamshanov.mission.project.missionproject.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import ru.kyamshanov.mission.project.missionproject.entity.SubTaskEntity
import ru.kyamshanov.mission.project.missionproject.models.UserId

interface SubTaskCrudRepository : CoroutineCrudRepository<SubTaskEntity, String> {

    fun findAllByTaskId(taskId: String): Flow<SubTaskEntity>

    @Query("UPDATE subtasks SET execution_result = :executionResult WHERE id = :taskId AND responsible = :userId RETURNING *;")
    fun setExecutionResult(userId: UserId, subtaskId: String, executionResult: String): Flow<SubTaskEntity>
}