package ru.kyamshanov.mission.project.missionproject.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import ru.kyamshanov.mission.project.missionproject.entity.LightTaskEntity
import ru.kyamshanov.mission.project.missionproject.entity.TaskEntity

interface TaskCrudRepository : CoroutineCrudRepository<TaskEntity, String> {

    fun findAllByProjectId(projectId: String): Flow<TaskEntity>

    @Query("SELECT id, title, create_at FROM tasks WHERE project_id = :projectId")
    fun findAllLightTasks(projectId: String): Flow<LightTaskEntity>

    @Query("UPDATE tasks SET points = :count WHERE id = :taskId and max_points >= :count RETURNING *;")
    fun setTaskPoints(taskId: String, count: Int): Flow<TaskEntity>
}