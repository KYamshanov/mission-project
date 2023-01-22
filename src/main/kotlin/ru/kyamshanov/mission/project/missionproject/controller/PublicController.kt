package ru.kyamshanov.mission.project.missionproject.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.kyamshanov.mission.project.missionproject.dto.*
import ru.kyamshanov.mission.project.missionproject.models.*
import ru.kyamshanov.mission.project.missionproject.service.ProjectCreatorService
import ru.kyamshanov.mission.project.missionproject.service.ProjectStageService
import ru.kyamshanov.mission.project.missionproject.service.TaskService
import ru.kyamshanov.mission.project.missionproject.service.TeamService

/**
 * Контроллер для end-point`ов админ. задач
 */
@RestController
@RequestMapping("/project")
class PublicController @Autowired constructor(
    private val projectCreatorService: ProjectCreatorService,
    private val teamService: TeamService,
    private val stageService: ProjectStageService,
    private val taskService: TaskService
) {

    @PostMapping("get/all")
    suspend fun registration(
        @RequestBody(required = true) body: CreateProjectRqDto
    ): ResponseEntity<CreateProjectRsDto> {
        val projectModel = ProjectModel(title = body.title, description = body.description)
        val responseModel = projectCreatorService.createProject(projectModel).let {
            CreateProjectRsDto(
                id = requireNotNull(it.id) { "Saved entity has no Id" },
                title = it.title,
                description = it.description
            )
        }
        return ResponseEntity(responseModel, HttpStatus.OK)
    }
}