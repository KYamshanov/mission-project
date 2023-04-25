package ru.kyamshanov.mission.project.missionproject.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.kyamshanov.mission.project.missionproject.dto.*
import ru.kyamshanov.mission.project.missionproject.models.*
import ru.kyamshanov.mission.project.missionproject.processor.*
import ru.kyamshanov.mission.project.missionproject.processor.FindingProcessor
import ru.kyamshanov.mission.project.missionproject.service.ProjectService
import ru.kyamshanov.mission.project.missionproject.service.SubtaskService
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
    private val taskService: TaskService,
    private val subtaskService: SubtaskService,
    private val subtaskCreationProcessor: SubtaskCreationProcessor,
    private val taskProcessor: TaskProcessor,
    private val subtaskProcessor: SubtaskProcessor,
    private val editProcessor: EditProcessor,
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
        val response = taskProcessor.getTask(userId, taskId)
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

    @PostMapping("subtask")
    suspend fun createSubtask(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestBody(required = true) body: CreateSubTaskRqDto
    ): ResponseEntity<CreateSubTaskRsDto> {
        val response = subtaskCreationProcessor.createSubtask(userId, body)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("subtasks")
    suspend fun getSubtaskByTaskId(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestParam(required = true, value = "taskId") taskId: String
    ): ResponseEntity<GetSubTaskRsDto> {
        val response = subtaskProcessor.getSubtasks(userId, taskId)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("subtask")
    suspend fun getSubtaskBySubtaskId(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestParam(required = true, value = "id") subtaskId: String
    ): ResponseEntity<SubtaskDto> {
        val response = subtaskProcessor.getSubtask(userId, subtaskId)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("subtask/edit")
    suspend fun editSubtask(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestBody(required = true) body: EditSubtaskRqDto
    ): ResponseEntity<Unit> {
        editProcessor.editSubtask(userId, body)
        return ResponseEntity(HttpStatus.OK)
    }


    private companion object {
        const val USER_ID_HEADER_KEY = "user-id"
    }
}