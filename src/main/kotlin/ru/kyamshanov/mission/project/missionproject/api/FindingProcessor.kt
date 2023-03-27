package ru.kyamshanov.mission.project.missionproject.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.kyamshanov.mission.project.missionproject.dto.FindProjectRsDto
import ru.kyamshanov.mission.project.missionproject.dto.ProjectInfoDto
import ru.kyamshanov.mission.project.missionproject.dto.toDto
import ru.kyamshanov.mission.project.missionproject.service.ProjectService
import ru.kyamshanov.mission.project.missionproject.service.TaskService
import ru.kyamshanov.mission.project.missionproject.service.TeamService

internal interface FindingProcessor {

    suspend fun getProject(projectId: String): ProjectInfoDto
}

@Component
private class FindingProcessorImpl @Autowired constructor(
    private val projectService: ProjectService,
    private val teamService: TeamService,
    private val taskService: TaskService
) : FindingProcessor {
    override suspend fun getProject(projectId: String): ProjectInfoDto {
        val project = projectService.getProject(projectId)
        val tasks = taskService.getTasks(projectId)
        return ProjectInfoDto(
            id = requireNotNull(project.id) { "Project id cannot be null from received it" },
            title = project.title,
            description = project.description,
            tasks = tasks.map { it.toDto() }
        )
    }

}