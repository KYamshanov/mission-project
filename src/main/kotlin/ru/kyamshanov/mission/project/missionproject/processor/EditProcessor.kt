package ru.kyamshanov.mission.project.missionproject.processor

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.kyamshanov.mission.project.missionproject.dto.EditProjectRqDto
import ru.kyamshanov.mission.project.missionproject.dto.EditSubtaskRqDto
import ru.kyamshanov.mission.project.missionproject.dto.EditTaskRqDto
import ru.kyamshanov.mission.project.missionproject.dto.toDomain
import ru.kyamshanov.mission.project.missionproject.models.*
import ru.kyamshanov.mission.project.missionproject.service.ProjectService
import ru.kyamshanov.mission.project.missionproject.service.SubtaskService
import ru.kyamshanov.mission.project.missionproject.service.TaskService
import java.time.LocalDateTime

interface EditProcessor {

    suspend fun editProject(request: EditProjectRqDto)

    suspend fun editTask(request: EditTaskRqDto)

    suspend fun editTasks(request: List<EditTaskRqDto>)

    suspend fun editSubtask(requester: UserId, request: EditSubtaskRqDto)
}

@Component
class EditProcessorImpl(
    private val projectService: ProjectService,
    private val taskService: TaskService,
    private val subtaskService: SubtaskService,
) : EditProcessor {
    override suspend fun editProject(request: EditProjectRqDto) {
        val projectModel = ProjectModel(
            id = request.projectId,
            title = request.title.orEmpty(),
            description = request.description.orEmpty(),
        )
        val editedScheme = ProjectEditingScheme(
            titleEdited = request.title != null,
            descriptionEdited = request.description != null
        )
        projectService.editProject(projectModel = projectModel, editedScheme = editedScheme)
    }

    override suspend fun editTask(request: EditTaskRqDto) {
        val mockTime = LocalDateTime.now()
        val model = TaskModel(
            id = request.taskId,
            title = request.title.orEmpty(),
            projectId = "",
            text = request.description.orEmpty(),
            createAt = mockTime,
            stage = TaskStage.FINISHED,
            startAt = request.startAt ?: mockTime,
            endAt = request.endAt ?: mockTime,
            maxPoints = request.maxPoints ?: 0,
            points = request.points ?: 0
        )
        val editedScheme = TaskEditingScheme(
            titleEdited = request.title != null,
            descriptionEdited = request.description != null,
            startAtEdited = request.startAt != null,
            endAtEdited = request.endAt != null,
            pointsEdited = request.points != null,
            maxPointsEdited = request.maxPoints != null
        )
        taskService.editTask(taskModel = model, editedScheme = editedScheme)
    }

    @Transactional
    override suspend fun editTasks(request: List<EditTaskRqDto>) {
        request.forEach { editTaskRqDto -> editTask(editTaskRqDto) }
    }

    override suspend fun editSubtask(requester: UserId, request: EditSubtaskRqDto) {
        val mockTime = LocalDateTime.now()
        val model = SubtaskModel(
            taskId = "",
            title = request.title.orEmpty(),
            description = request.description.orEmpty(),
            createAt = mockTime,
            startAt = request.startAt ?: mockTime,
            endAt = request.endAt ?: mockTime,
            responsible = UserInfo(userId = request.responsible.orEmpty(), ""),
            stage = request.state?.toDomain() ?: SubtaskModel.Stage.CREATED,
            executionResult = request.executionResult,
            id = request.subtaskId
        )
        val editedScheme = SubtaskEditingScheme(
            titleEdited = request.title != null,
            descriptionEdited = request.description != null,
            startAtEdited = request.startAt != null,
            endAtEdited = request.endAt != null,
            responsibleEdited = request.responsible != null,
            stateEdited = request.state != null,
            executionResultEdited = request.executionResult != null
        )
        subtaskService.editSubtask(requester = requester, subtaskModel = model, editingScheme = editedScheme)
    }

}