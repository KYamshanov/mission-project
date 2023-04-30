package ru.kyamshanov.mission.project.missionproject.service

import kotlinx.coroutines.flow.toCollection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.kyamshanov.mission.project.missionproject.entity.*
import ru.kyamshanov.mission.project.missionproject.exception.TaskException
import ru.kyamshanov.mission.project.missionproject.models.*
import ru.kyamshanov.mission.project.missionproject.repository.ShortTaskCrudRepository
import ru.kyamshanov.mission.project.missionproject.repository.TaskCrudRepository
import ru.kyamshanov.mission.project.missionproject.repository.TaskRepository
import java.time.LocalDateTime

interface TaskService {

    suspend fun createTask(taskModel: TaskModel): TaskModel

    suspend fun getTasks(projectId: String): Collection<ShortTaskModel>

    suspend fun getLightTasks(projectId: String): Collection<LightTaskModel>

    suspend fun getTask(taskId: String): TaskModel

    suspend fun setTaskPoints(taskId: String, count: Int)

    suspend fun editTask(taskModel: TaskModel, editedScheme: TaskEditingScheme)
}

@Service
class TaskServiceImpl @Autowired constructor(
    private val taskCrudRepository: TaskCrudRepository,
    private val shortTaskCrudRepository: ShortTaskCrudRepository,
    private val taskRepository: TaskRepository,
) : TaskService {
    override suspend fun createTask(taskModel: TaskModel): TaskModel =
        taskCrudRepository.save(taskModel.toEntity()).let { it.toModel(resolveStage(it)) }

    override suspend fun getTasks(projectId: String): Collection<ShortTaskModel> =
        shortTaskCrudRepository.findAllByProjectId(projectId).toCollection(mutableListOf())
            .map { it.toDomain(resolveStage(it)) }

    override suspend fun getLightTasks(projectId: String): Collection<LightTaskModel> =
        taskCrudRepository.findAllLightTasks(projectId).toCollection(mutableListOf()).map { it.toModel() }

    override suspend fun getTask(taskId: String): TaskModel =
        taskCrudRepository.findById(taskId)?.let { entry -> entry.toModel(resolveStage(entry)) }
            ?: throw TaskException("Task with is $taskId has no found")

    @Transactional
    override suspend fun setTaskPoints(taskId: String, count: Int) {
        val updatedEntitiesCount = taskCrudRepository.setTaskPoints(taskId, count).toCollection(mutableListOf()).count()
        if (updatedEntitiesCount != 1) throw IllegalStateException("More or less than 1 object has been updated. [$updatedEntitiesCount]")
    }

    @Transactional
    override suspend fun editTask(taskModel: TaskModel, editedScheme: TaskEditingScheme) {
        val updatedEntities = taskRepository.updateTask(taskModel.toEntity(), editedScheme).toCollection(mutableListOf())
        assert(updatedEntities.size == 1){ "Was saved less or more then 1 entity" }
    }

    private fun resolveStage(taskEntity: TaskEntity): TaskStage {
        val nowTime = LocalDateTime.now()
        return when {
            nowTime < taskEntity.startAt -> TaskStage.WAIT
            nowTime > taskEntity.endAt -> TaskStage.FINISHED
            else -> TaskStage.IN_PROGRESS
        }
    }

    private fun resolveStage(taskEntity: ShortTaskEntity): TaskStage {
        val nowTime = LocalDateTime.now()
        return when {
            nowTime < taskEntity.startAt -> TaskStage.WAIT
            nowTime > taskEntity.endAt -> TaskStage.FINISHED
            else -> TaskStage.IN_PROGRESS
        }
    }
}