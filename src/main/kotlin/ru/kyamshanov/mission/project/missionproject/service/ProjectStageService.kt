package ru.kyamshanov.mission.project.missionproject.service

import kotlinx.coroutines.flow.toCollection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.kyamshanov.mission.project.missionproject.entity.toEntity
import ru.kyamshanov.mission.project.missionproject.entity.toModel
import ru.kyamshanov.mission.project.missionproject.exception.ProjectException
import ru.kyamshanov.mission.project.missionproject.models.ProjectStage
import ru.kyamshanov.mission.project.missionproject.repository.StageHistoryCrudRepository

interface ProjectStageService {

    suspend fun setProjectStage(projectId: String, stage: ProjectStage)

    suspend fun getProjectStage(projectId: String): ProjectStage

    suspend fun getStageHistory(projectId: String): List<ProjectStage>

}


@Service
class ProjectStageServiceImpl @Autowired constructor(
    private val stageHistoryCrudRepository: StageHistoryCrudRepository
) : ProjectStageService {
    override suspend fun setProjectStage(projectId: String, stage: ProjectStage) {
        stageHistoryCrudRepository.save(stage.toEntity(projectId))
    }

    override suspend fun getProjectStage(projectId: String): ProjectStage =
        stageHistoryCrudRepository.getLatestStage(projectId)?.toModel()
            ?: throw ProjectException("Project with id $projectId has not found")

    override suspend fun getStageHistory(projectId: String): List<ProjectStage> =
        stageHistoryCrudRepository.findAllByProjectId(projectId).toCollection(mutableListOf())
            .map { it.toModel() }

}