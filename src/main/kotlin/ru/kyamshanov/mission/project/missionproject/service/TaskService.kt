package ru.kyamshanov.mission.project.missionproject.service

import kotlinx.coroutines.flow.toCollection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.kyamshanov.mission.project.missionproject.entity.*
import ru.kyamshanov.mission.project.missionproject.exception.TaskException
import ru.kyamshanov.mission.project.missionproject.models.LightTaskModel
import ru.kyamshanov.mission.project.missionproject.models.ShortTaskModel
import ru.kyamshanov.mission.project.missionproject.models.TaskModel
import ru.kyamshanov.mission.project.missionproject.models.TaskStage
import ru.kyamshanov.mission.project.missionproject.repository.ShortTaskCrudRepository
import ru.kyamshanov.mission.project.missionproject.repository.TaskCrudRepository
import java.time.LocalDateTime

interface TaskService {

    suspend fun createTask(taskModel: TaskModel): TaskModel

    suspend fun getTasks(projectId: String): Collection<ShortTaskModel>

    suspend fun getLightTasks(projectId: String): Collection<LightTaskModel>

    suspend fun getTask(taskId: String): TaskModel

    suspend fun setTaskPoints(taskId: String, count: Int)
}

@Service
class TaskServiceImpl @Autowired constructor(
    private val taskCrudRepository: TaskCrudRepository,
    private val shortTaskCrudRepository: ShortTaskCrudRepository
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

    override suspend fun setTaskPoints(taskId: String, count: Int) {
        TODO("It has not implementation")
        /*  val taskStage = resolveStage(requireNotNull(taskCrudRepository.findById(taskId)){ "Stage is absent" })
          if(  != TaskModel.Stage.FINISHED) throw IllegalStateException("Task is not finished")
  */
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