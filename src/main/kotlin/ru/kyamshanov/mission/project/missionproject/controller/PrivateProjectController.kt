package ru.kyamshanov.mission.project.missionproject.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.kyamshanov.mission.project.missionproject.api.FindingProcessor
import ru.kyamshanov.mission.project.missionproject.dto.*
import ru.kyamshanov.mission.project.missionproject.models.*
import ru.kyamshanov.mission.project.missionproject.processor.LoadProcessor
import ru.kyamshanov.mission.project.missionproject.processor.TeamProcessor
import ru.kyamshanov.mission.project.missionproject.service.ProjectService
import ru.kyamshanov.mission.project.missionproject.service.TaskService

/**
 * Контроллер для end-point`ов админ. задач
 */
@RestController
@RequestMapping("/project/private/")
internal class PrivateProjectController @Autowired constructor(
    private val loadProcessor: LoadProcessor,
    private val findingProcessor: FindingProcessor,
    private val projectService: ProjectService,
    private val teamProcessor: TeamProcessor,
    private val taskService: TaskService
) {

    @PostMapping("get/all")
    suspend fun loadProjects(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestBody(required = true) body: GetAllProjectsRqDto
    ): ResponseEntity<GetAllProjectsRsDto> {
        val response = loadProcessor.searchByName(body)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("find")
    suspend fun find(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestParam(required = true, value = "id") id: String
    ): ResponseEntity<ProjectInfoDto> {
        val response = findingProcessor.getProject(id)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("team")
    suspend fun getTeam(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestParam(required = true, value = "project") projectId: String
    ): ResponseEntity<GetTeamRsDto> {
        val team = teamProcessor.getTeam(userId, projectId)
        val response = GetTeamRsDto(projectId, team.participants.map { it.toDto() })
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("task/get")
    suspend fun getTask(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestParam(required = true, value = "task") taskId: String
    ): ResponseEntity<GetTaskRsDto> {
        val response = taskService.getTask(taskId)
            .let { taskModel ->
                GetTaskRsDto(
                    title = taskModel.title,
                    text = taskModel.text,
                    createdAt = taskModel.createAt,
                    taskStage = taskModel.stage.toDto(),
                    startAt = taskModel.startAt,
                    endAt = taskModel.endAt,
                    maxPoints = taskModel.maxPoints,
                    points = taskModel.points,
                    id = requireNotNull(taskModel.id)
                )
            }
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("task/get/all")
    suspend fun getAllTasks(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestParam(required = true, value = "project") projectId: String
    ): ResponseEntity<GetTasksRsDto> {
        val response = taskService.getLightTasks(projectId)
            .map { it.id }.let { GetTasksRsDto(it) }
        return ResponseEntity(response, HttpStatus.OK)
    }


    private companion object {
        const val USER_ID_HEADER_KEY = "user-id"
    }
}