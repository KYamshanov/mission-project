package ru.kyamshanov.mission.project.missionproject.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.kyamshanov.mission.project.missionproject.entity.toCreatedProjectInfo
import ru.kyamshanov.mission.project.missionproject.entity.toEntity
import ru.kyamshanov.mission.project.missionproject.entity.toModel
import ru.kyamshanov.mission.project.missionproject.exception.ProjectException
import ru.kyamshanov.mission.project.missionproject.models.ShortProjectInfo
import ru.kyamshanov.mission.project.missionproject.models.ProjectModel
import ru.kyamshanov.mission.project.missionproject.repository.ProjectCrudRepository

interface ProjectService {

    suspend fun createProject(projectModel: ProjectModel): ShortProjectInfo

    suspend fun getProject(id: String): ProjectModel

}

@Service
private class ProjectServiceImpl @Autowired constructor(
    private val projectCrudRepository: ProjectCrudRepository
) : ProjectService {
    override suspend fun createProject(projectModel: ProjectModel): ShortProjectInfo =
        projectCrudRepository.save(projectModel.toEntity()).toCreatedProjectInfo()

    override suspend fun getProject(id: String): ProjectModel =
        projectCrudRepository.findById(id)?.toModel() ?: throw ProjectException("Project with id $id have not found")

}