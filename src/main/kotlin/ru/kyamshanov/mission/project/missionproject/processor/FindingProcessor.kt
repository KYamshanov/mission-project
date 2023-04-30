package ru.kyamshanov.mission.project.missionproject.processor

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.kyamshanov.mission.project.missionproject.dto.ProjectInfoDto
import ru.kyamshanov.mission.project.missionproject.dto.ProjectStageDto
import ru.kyamshanov.mission.project.missionproject.dto.toDto
import ru.kyamshanov.mission.project.missionproject.models.TimeRestriction
import ru.kyamshanov.mission.project.missionproject.service.AvailabilityService
import ru.kyamshanov.mission.project.missionproject.service.ProjectService
import ru.kyamshanov.mission.project.missionproject.service.TaskService
import ru.kyamshanov.mission.project.missionproject.service.TeamService
import ru.kyamshanov.mission.project.missionproject.usecase.GetCurrentTaskUseCase
import ru.kyamshanov.mission.project.missionproject.usecase.GetTimeRestrictionUseCase
import java.time.LocalDateTime

internal interface FindingProcessor {

    suspend fun getProject(projectId: String): ProjectInfoDto
}

@Component
private class FindingProcessorImpl @Autowired constructor(
    private val projectService: ProjectService,
    private val teamService: TeamService,
    private val taskService: TaskService,
    private val getCurrentTaskUseCase: GetCurrentTaskUseCase,
    private val getTimeRestrictionUseCase: GetTimeRestrictionUseCase,
) : FindingProcessor {
    override suspend fun getProject(projectId: String): ProjectInfoDto {
        val project = projectService.getProject(projectId)
        val tasks = taskService.getTasks(projectId)
        val timeRestriction = getTimeRestrictionUseCase(tasks)
        val projectStageDto = timeRestriction?.let { getProjectStage(it) } ?: ProjectStageDto.WAIT
        return ProjectInfoDto(
            id = requireNotNull(project.id) { "Project id cannot be null from received it" },
            title = project.title,
            description = project.description,
            tasks = tasks.map { it.toDto() },
            currentTask = getCurrentTaskUseCase(tasks)?.toDto(false),
            startAt = timeRestriction?.startAt,
            endAt = timeRestriction?.endAt,
            stage = projectStageDto
        )
    }

    private fun getProjectStage(restriction: TimeRestriction): ProjectStageDto {
        val nowTime = LocalDateTime.now()
        return if (restriction.startAt > nowTime) return ProjectStageDto.WAIT
        else if (restriction.endAt < nowTime) return ProjectStageDto.FINISHED
        else ProjectStageDto.IN_PROGRESS
    }

}