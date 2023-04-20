package ru.kyamshanov.mission.project.missionproject.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toCollection
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.kyamshanov.mission.project.missionproject.converter.SuspendConverter
import ru.kyamshanov.mission.project.missionproject.entity.SubTaskEntity
import ru.kyamshanov.mission.project.missionproject.entity.toEntity
import ru.kyamshanov.mission.project.missionproject.entity.toModel
import ru.kyamshanov.mission.project.missionproject.models.SubtaskModel
import ru.kyamshanov.mission.project.missionproject.repository.SubTaskCrudRepository

interface SubtaskService {

    suspend fun create(subTaskModel: SubtaskModel): SubtaskModel

    suspend fun getSubTasks(taskId: String): List<SubtaskModel>

    suspend fun getSubtask(subtaskId: String): SubtaskModel

    suspend fun setExecutionResult(subtaskId: String, executionResult: String)
}

@Service
private class SubtaskServiceImpl(
    private val subTaskCrudRepository: SubTaskCrudRepository,
    private val subtaskEntityConverter: SuspendConverter<SubTaskEntity, SubtaskModel>
) : SubtaskService {
    override suspend fun create(subTaskModel: SubtaskModel): SubtaskModel =
        subtaskEntityConverter.convert(subTaskCrudRepository.save(subTaskModel.toEntity()))

    override suspend fun getSubTasks(taskId: String): List<SubtaskModel> =
        subTaskCrudRepository.findAllByTaskId(taskId).map { subtaskEntityConverter.convert(it) }
            .toCollection(mutableListOf())

    override suspend fun getSubtask(subtaskId: String): SubtaskModel =
        subtaskEntityConverter.convert(requireNotNull(subTaskCrudRepository.findById(subtaskId)) { "" })


    @Transactional
    override suspend fun setExecutionResult(subtaskId: String, executionResult: String) {
        val updatedEntities =
            subTaskCrudRepository.setExecutionResult(subtaskId, executionResult).toCollection(mutableListOf())
        if (updatedEntities.size != 1) throw IllegalStateException("The number of updated entities is not equal to 1")
    }

}