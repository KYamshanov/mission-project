package ru.kyamshanov.mission.project.missionproject.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.kyamshanov.mission.project.missionproject.processor.EditProcessor
import ru.kyamshanov.mission.project.missionproject.dto.*
import ru.kyamshanov.mission.project.missionproject.models.*
import ru.kyamshanov.mission.project.missionproject.processor.RoleProcessor
import ru.kyamshanov.mission.project.missionproject.service.ProjectService
import ru.kyamshanov.mission.project.missionproject.service.ProjectStageService
import ru.kyamshanov.mission.project.missionproject.service.TaskService
import ru.kyamshanov.mission.project.missionproject.service.TeamService
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Контроллер для end-point`ов админ. задач
 */
@RestController
@RequestMapping("/project/manager/")
internal class ManagerProjectController @Autowired constructor(
    private val projectService: ProjectService,
    private val teamService: TeamService,
    private val stageService: ProjectStageService,
    private val taskService: TaskService,
    private val editProcessor: EditProcessor,
    private val roleProcessor: RoleProcessor,
) {

    @PostMapping("create")
    suspend fun registration(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestBody(required = true) body: CreateProjectRqDto
    ): ResponseEntity<CreateProjectRsDto> {
        val projectModel = ProjectModel(title = body.title, description = body.description)
        val responseModel = CreateProjectRsDto(
            id = requireNotNull(projectService.createProject(projectModel).id) { "Saved entity has no Id" }
        )
        return ResponseEntity(responseModel, HttpStatus.OK)
    }

    @PostMapping("attach")
    suspend fun attachTeam(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestBody(required = true) body: AttachTeamRqDto
    ): ResponseEntity<Unit> {
        val team =
            Team(body.participants.map { participantId ->
                Participant(
                    role = Participant.Role.PARTICIPANT,
                    userInfo = UserInfo(userId = participantId, userName = "")
                )
            })
        teamService.attachTeam(body.project, team)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("team")
    suspend fun getTeam(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestParam(required = true, value = "project") projectId: String
    ): ResponseEntity<GetTeamRsDto> {
        val response = GetTeamRsDto(projectId, teamService.getTeam(projectId).participants.map { it.toDto() })
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("role")
    suspend fun setRole(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestBody(required = true) body: SetRoleRqDto
    ): ResponseEntity<Unit> {
        roleProcessor.setRole(
            projectId = body.projectId,
            userId = body.userId,
            role = body.role.toDomain()
        )
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping("task/set_points")
    suspend fun setPoints(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestBody(required = true) body: SetTaskPointsRqDto
    ): ResponseEntity<Unit> {
        taskService.setTaskPoints(body.taskId,body.points)
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping("participant/add")
    suspend fun addParticipant(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestBody(required = true) body: AddParticipantRqDto
    ): ResponseEntity<Unit> {
        teamService.addParticipant(
            projectId = body.projectId,
            participant = Participant(
                role = body.role.toDomain(),
                userInfo = UserInfo(userId = body.userId, userName = "")
            )
        )
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping("participant/remove")
    suspend fun removeParticipant(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestBody(required = true) body: RemoveParticipantRqDto
    ): ResponseEntity<Unit> {
        teamService.removeParticipant(
            projectId = body.projectId,
            participant = Participant(
                role = Participant.Role.PARTICIPANT,
                userInfo = UserInfo(userId = body.userId, userName = "")
            )
        )
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping("project/edit")
    suspend fun editProject(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestBody(required = true) body: EditProjectRqDto
    ): ResponseEntity<CreateProjectRsDto> {
        editProcessor.editProject(body)
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping("task/create")
    suspend fun createTask(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestBody(required = true) body: CreateTaskRqDto
    ): ResponseEntity<CreateTaskRsDto> {
        val taskModel = TaskModel(
            projectId = body.projectId,
            title = body.title,
            text = body.text,
            createAt = LocalDateTime.now(),
            startAt = LocalDateTime.ofInstant(body.startAt.toInstant(), ZoneId.systemDefault()),
            endAt = LocalDateTime.ofInstant(body.endAt.toInstant(), ZoneId.systemDefault()),
            maxPoints = body.maxPoints,
            stage = TaskStage.WAIT
        )

        val response = CreateTaskRsDto(
            taskId = requireNotNull(taskService.createTask(taskModel).id) { "Saved task (title: ${body.title} has id equal null." },
        )
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("task/edit")
    suspend fun editTask(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestBody(required = true) body: EditTaskRqDto
    ): ResponseEntity<CreateProjectRsDto> {
        editProcessor.editTask(body)
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping("task/edit/set")
    suspend fun editTaskSet(
        @RequestHeader(value = USER_ID_HEADER_KEY, required = true) userId: String,
        @RequestBody(required = true) body: EditTaskSetRqDto
    ): ResponseEntity<CreateProjectRsDto> {
        editProcessor.editTasks(body)
        return ResponseEntity(HttpStatus.OK)
    }

    private companion object {
        const val USER_ID_HEADER_KEY = "user-id"
    }
}