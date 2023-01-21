package ru.kyamshanov.mission.project.missionproject.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.kyamshanov.mission.project.missionproject.entity.toEntity
import ru.kyamshanov.mission.project.missionproject.entity.toModel
import ru.kyamshanov.mission.project.missionproject.exception.ProjectException
import ru.kyamshanov.mission.project.missionproject.models.ProjectModel
import ru.kyamshanov.mission.project.missionproject.repository.ProjectCrudRepository

interface ProjectCreatorService {

    suspend fun createProject(projectModel: ProjectModel): ProjectModel

    suspend fun getProject(id: String): ProjectModel

}

@Service
private class ProjectCreatorServiceImpl @Autowired constructor(
    private val projectCrudRepository: ProjectCrudRepository
) : ProjectCreatorService {
    override suspend fun createProject(projectModel: ProjectModel): ProjectModel =
        projectCrudRepository.save(projectModel.toEntity()).toModel()

    override suspend fun getProject(id: String): ProjectModel =
        projectCrudRepository.findById(id)?.toModel() ?: throw ProjectException("Project with id $id have not found")

}