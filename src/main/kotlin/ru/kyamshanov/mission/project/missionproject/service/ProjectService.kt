package ru.kyamshanov.mission.project.missionproject.service

import kotlinx.coroutines.flow.toCollection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.kyamshanov.mission.project.missionproject.entity.toCreatedProjectInfo
import ru.kyamshanov.mission.project.missionproject.entity.toEntity
import ru.kyamshanov.mission.project.missionproject.entity.toModel
import ru.kyamshanov.mission.project.missionproject.exception.ProjectException
import ru.kyamshanov.mission.project.missionproject.models.ProjectEditingScheme
import ru.kyamshanov.mission.project.missionproject.models.ShortProjectInfo
import ru.kyamshanov.mission.project.missionproject.models.ProjectModel
import ru.kyamshanov.mission.project.missionproject.repository.ProjectCrudRepository
import ru.kyamshanov.mission.project.missionproject.repository.ProjectRepository

interface ProjectService {

    suspend fun createProject(projectModel: ProjectModel): ShortProjectInfo

    suspend fun getProject(id: String): ProjectModel

    suspend fun editProject(projectModel: ProjectModel, editedScheme: ProjectEditingScheme)
}

@Service
class ProjectServiceImpl @Autowired constructor(
    private val projectCrudRepository: ProjectCrudRepository,
    private val projectRepository: ProjectRepository
) : ProjectService {
    override suspend fun createProject(projectModel: ProjectModel): ShortProjectInfo =
        projectCrudRepository.save(projectModel.toEntity()).toCreatedProjectInfo()

    override suspend fun getProject(id: String): ProjectModel =
        projectCrudRepository.findById(id)?.toModel() ?: throw ProjectException("Project with id $id have not found")

    @Transactional
    override suspend fun editProject(projectModel: ProjectModel, editedScheme: ProjectEditingScheme) {
        val updatedEntities = projectRepository.updateProject(projectModel.toEntity(), editedScheme).toCollection(mutableListOf())
        assert(updatedEntities.size == 1){ "Was saved less or more then 1 entity" }
    }

}