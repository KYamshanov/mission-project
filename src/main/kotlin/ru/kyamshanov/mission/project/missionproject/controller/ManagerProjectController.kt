package ru.kyamshanov.mission.project.missionproject.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.kyamshanov.mission.project.missionproject.api.FindingProcessor
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
@RequestMapping("/project/manager/")
internal class ManagerProjectController @Autowired constructor(
    private val projectCreatorService: ProjectCreatorService,
    private val teamService: TeamService,
    private val stageService: ProjectStageService,
    private val taskService: TaskService,
    private val findingProcessor: FindingProcessor
) {

    @PostMapping("create")
    suspend fun registration(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestBody(required = true) body: CreateProjectRqDto
    ): ResponseEntity<CreateProjectRsDto> {
        val projectModel = ProjectModel(title = body.title, description = body.description)
        val responseModel = CreateProjectRsDto(
            id = requireNotNull(projectCreatorService.createProject(projectModel).id) { "Saved entity has no Id" }
        )
        return ResponseEntity(responseModel, HttpStatus.OK)
    }

    @GetMapping("find")
    suspend fun find(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestParam(required = true, value = "id") id: String
    ): ResponseEntity<FindProjectRsDto> {
        val response = findingProcessor.getProject(id)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("attach")
    suspend fun attachTeam(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestBody(required = true) body: AttachTeamRqDto
    ): ResponseEntity<Unit> {
        val team =
            Team(body.participants.map { participantId -> Participant(participantId, Participant.Role.PARTICIPANT) })
        teamService.attachTeam(body.project, team)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("team")
    suspend fun attachTeam(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestParam(required = true, value = "project") projectId: String
    ): ResponseEntity<GetTeamRsDto> {
        val response = GetTeamRsDto(projectId, teamService.getTeam(projectId).participants)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("role")
    suspend fun setRole(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestBody(required = true) body: SetRoleRqDto
    ): ResponseEntity<Unit> {
        teamService.addParticipant(body.projectId, Participant(body.userId, body.role))
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping("stage")
    suspend fun setStage(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestBody(required = true) body: SetStageRqDto
    ): ResponseEntity<Unit> {
        stageService.setProjectStage(body.projectId, ProjectStage(body.stage))
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("stage")
    suspend fun getStage(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestParam(required = true, value = "project") id: String
    ): ResponseEntity<ProjectStageRsDto> {
        val response = ProjectStageRsDto(stageService.getProjectStage(id).toDto())
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("stage/history")
    suspend fun getStageHistory(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestParam(required = true, value = "project") id: String
    ): ResponseEntity<HistoryRsDto> {
        val response = HistoryRsDto(stageService.getStageHistory(id).map { it.toDto() })
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("task/create")
    suspend fun createTask(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestBody(required = true) body: CreateTaskRqDto
    ): ResponseEntity<CreateTaskRsDto> {
        val taskModel = TaskModel(
            projectId = body.projectId,
            title = body.title,
            text = body.text
        )
        val response = taskService.createTask(taskModel)
            .let {
                CreateTaskRsDto(
                    taskId = requireNotNull(it.id) { "Saved task (title: ${body.title} has id equal null." },
                    createdAt = it.createAt
                )
            }
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("task/get")
    suspend fun getTask(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestParam(required = true, value = "task") taskId: String
    ): ResponseEntity<GetTaskRsDto> {
        val response = taskService.getTask(taskId)
            .let {
                GetTaskRsDto(
                    title = it.title,
                    text = it.text,
                    createdAt = it.createAt
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