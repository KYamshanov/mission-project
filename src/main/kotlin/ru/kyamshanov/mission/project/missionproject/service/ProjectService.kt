package ru.kyamshanov.mission.project.missionproject.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.toCollection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.kyamshanov.mission.project.missionproject.entity.ProjectRestrictedEntity
import ru.kyamshanov.mission.project.missionproject.entity.toCreatedProjectInfo
import ru.kyamshanov.mission.project.missionproject.entity.toEntity
import ru.kyamshanov.mission.project.missionproject.entity.toModel
import ru.kyamshanov.mission.project.missionproject.exception.ProjectException
import ru.kyamshanov.mission.project.missionproject.models.*
import ru.kyamshanov.mission.project.missionproject.repository.ProjectCrudRepository
import ru.kyamshanov.mission.project.missionproject.repository.ProjectRepository
import ru.kyamshanov.mission.project.missionproject.repository.ProjectRestrictedRepository
import ru.kyamshanov.mission.project.missionproject.usecase.GetTaskStageUseCase
import ru.kyamshanov.mission.project.missionproject.usecase.GetUserRoleUseCase

interface ProjectService {

    suspend fun createProject(projectModel: ProjectModel): ShortProjectInfo

    suspend fun getProject(id: String): ProjectModel

    suspend fun editProject(projectModel: ProjectModel, editedScheme: ProjectEditingScheme)

    suspend fun getAttachedProjects(userId: UserId): List<ProjectRestrictedModel>
}

@Service
class ProjectServiceImpl @Autowired constructor(
    private val projectCrudRepository: ProjectCrudRepository,
    private val projectRepository: ProjectRepository,
    private val projectRestrictedRepository: ProjectRestrictedRepository,
    private val getTaskStageUseCase: GetTaskStageUseCase,
    private val getUserRoleUseCase: GetUserRoleUseCase,
) : ProjectService {
    override suspend fun createProject(projectModel: ProjectModel): ShortProjectInfo =
        projectCrudRepository.save(projectModel.toEntity()).toCreatedProjectInfo()

    override suspend fun getProject(id: String): ProjectModel =
        projectCrudRepository.findById(id)?.toModel() ?: throw ProjectException("Project with id $id have not found")

    @Transactional
    override suspend fun editProject(projectModel: ProjectModel, editedScheme: ProjectEditingScheme) {
        val updatedEntities =
            projectRepository.updateProject(projectModel.toEntity(), editedScheme).toCollection(mutableListOf())
        assert(updatedEntities.size == 1) { "Was saved less or more then 1 entity" }
    }

    override suspend fun getAttachedProjects(userId: UserId): List<ProjectRestrictedModel> =
        projectRestrictedRepository.getUserProjects(userId).mapNotNull { it.toModel(userId) }
            .toCollection(mutableListOf())


    private suspend fun ProjectRestrictedEntity.toModel(userId: UserId): ProjectRestrictedModel {
        val taskStage = getTaskStageUseCase(startAt, endAt)
        return ProjectRestrictedModel(
            projectId = projectId,
            title = title,
            description = description,
            startAt = startAt,
            endAt = endAt,
            stage = taskStage,
            userRole = getUserRoleUseCase.invoke(userId, projectId)
        )
    }
}