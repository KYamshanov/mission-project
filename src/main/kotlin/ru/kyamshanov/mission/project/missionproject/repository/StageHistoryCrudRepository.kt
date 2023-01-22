package ru.kyamshanov.mission.project.missionproject.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import ru.kyamshanov.mission.project.missionproject.entity.StageHistoryEntity

interface StageHistoryCrudRepository : CoroutineCrudRepository<StageHistoryEntity, String> {

    @Query("SELECT * FROM stage_history WHERE project_id = :projectId ORDER BY updatedAt DESC LIMIT 1")
    suspend fun getLatestStage(projectId: String): StageHistoryEntity?

    fun findAllByProjectId(projectId: String): Flow<StageHistoryEntity>
}