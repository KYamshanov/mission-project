package ru.kyamshanov.mission.project.missionproject.service

import kotlinx.coroutines.flow.toCollection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.kyamshanov.mission.project.missionproject.entity.toEntity
import ru.kyamshanov.mission.project.missionproject.entity.toModel
import ru.kyamshanov.mission.project.missionproject.exception.TaskException
import ru.kyamshanov.mission.project.missionproject.models.LightTaskModel
import ru.kyamshanov.mission.project.missionproject.models.TaskModel
import ru.kyamshanov.mission.project.missionproject.repository.TaskCrudRepository

interface TaskService {

    suspend fun createTask(taskModel: TaskModel): TaskModel

    suspend fun getTasks(projectId: String): Collection<TaskModel>

    suspend fun getLightTasks(projectId: String): Collection<LightTaskModel>

    suspend fun getTask(taskId: String): TaskModel
}

@Service
class TaskServiceImpl @Autowired constructor(
    private val taskCrudRepository: TaskCrudRepository
) : TaskService {
    override suspend fun createTask(taskModel: TaskModel): TaskModel =
        taskCrudRepository.save(taskModel.toEntity()).toModel()

    override suspend fun getTasks(projectId: String): Collection<TaskModel> =
        taskCrudRepository.findAllByProjectId(projectId).toCollection(mutableListOf()).map { it.toModel() }

    override suspend fun getLightTasks(projectId: String): Collection<LightTaskModel> =
        taskCrudRepository.findAllLightTasks(projectId).toCollection(mutableListOf()).map { it.toModel() }

    override suspend fun getTask(taskId: String): TaskModel =
        taskCrudRepository.findById(taskId)?.toModel() ?: throw TaskException("Task with is $taskId has no found")

}