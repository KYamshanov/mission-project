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
import ru.kyamshanov.mission.project.missionproject.dto.AttachTeamRqDto
import ru.kyamshanov.mission.project.missionproject.dto.CreateProjectRqDto
import ru.kyamshanov.mission.project.missionproject.dto.CreateProjectRsDto
import ru.kyamshanov.mission.project.missionproject.dto.GetTeamRsDto
import ru.kyamshanov.mission.project.missionproject.models.ProjectModel
import ru.kyamshanov.mission.project.missionproject.models.Team
import ru.kyamshanov.mission.project.missionproject.service.ProjectCreatorService
import ru.kyamshanov.mission.project.missionproject.service.TeamService

/**
 * Контроллер для end-point`ов админ. задач
 */
@RestController
@RequestMapping("/project/private/admin")
class AdminController @Autowired constructor(
    private val projectCreatorService: ProjectCreatorService,
    private val teamService: TeamService
) {

    @PostMapping("create")
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

    @GetMapping("find")
    suspend fun find(
        @RequestParam(required = true, value = "id") id: String
    ): ResponseEntity<CreateProjectRsDto> {
        val responseModel = projectCreatorService.getProject(id).let {
            CreateProjectRsDto(
                id = requireNotNull(it.id) { "Saved entity has no Id" },
                title = it.title,
                description = it.description
            )
        }
        return ResponseEntity(responseModel, HttpStatus.OK)
    }

    @PostMapping("attach")
    suspend fun attachTeam(
        @RequestBody(required = true) body: AttachTeamRqDto
    ): ResponseEntity<Unit> {
        teamService.attachTeam(body.project, Team(body.participants))
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("team")
    suspend fun attachTeam(
        @RequestParam(required = true, value = "project") projectId: String
    ): ResponseEntity<GetTeamRsDto> {
        val response = GetTeamRsDto(projectId, teamService.getTeam(projectId).participants)
        return ResponseEntity(response,HttpStatus.OK)
    }
}