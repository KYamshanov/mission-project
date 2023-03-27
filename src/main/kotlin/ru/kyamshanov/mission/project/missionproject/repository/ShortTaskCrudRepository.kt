package ru.kyamshanov.mission.project.missionproject.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import ru.kyamshanov.mission.project.missionproject.entity.LightTaskEntity
import ru.kyamshanov.mission.project.missionproject.entity.ShortTaskEntity
import ru.kyamshanov.mission.project.missionproject.entity.TaskEntity

interface ShortTaskCrudRepository : CoroutineCrudRepository<ShortTaskEntity, String> {

    fun findAllByProjectId(projectId: String): Flow<ShortTaskEntity>
}