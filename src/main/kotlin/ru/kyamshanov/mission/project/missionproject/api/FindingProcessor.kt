package ru.kyamshanov.mission.project.missionproject.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.kyamshanov.mission.project.missionproject.dto.FindProjectRsDto
import ru.kyamshanov.mission.project.missionproject.dto.toDto
import ru.kyamshanov.mission.project.missionproject.service.ProjectCreatorService
import ru.kyamshanov.mission.project.missionproject.service.TeamService

internal interface FindingProcessor {

    suspend fun getProject(projectId: String): FindProjectRsDto
}

@Component
private class FindingProcessorImpl @Autowired constructor(
    private val projectCreatorService: ProjectCreatorService,
    private val teamService: TeamService,
) : FindingProcessor {
    override suspend fun getProject(projectId: String): FindProjectRsDto {
        val project = projectCreatorService.getProject(projectId)
        val team = teamService.getTeam(projectId)
        return FindProjectRsDto(
            id = requireNotNull(project.id) { "Project id cannot be null from received it" },
            title = project.title,
            description = project.description,
            participants = team.participants.map { it.toDto() }
        )
    }

}