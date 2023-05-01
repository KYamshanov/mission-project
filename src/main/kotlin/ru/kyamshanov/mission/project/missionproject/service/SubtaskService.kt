package ru.kyamshanov.mission.project.missionproject.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toCollection
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.kyamshanov.mission.project.missionproject.converter.SuspendConverter
import ru.kyamshanov.mission.project.missionproject.entity.SubTaskEntity
import ru.kyamshanov.mission.project.missionproject.entity.toEntity
import ru.kyamshanov.mission.project.missionproject.models.*
import ru.kyamshanov.mission.project.missionproject.repository.SubTaskCrudRepository
import ru.kyamshanov.mission.project.missionproject.repository.SubtaskRepository

interface SubtaskService {

    suspend fun create(requester: UserId, subTaskModel: SubtaskModel): SubtaskModel

    suspend fun getSubTasks(taskId: String): List<SubtaskModel>

    suspend fun getSubtask(subtaskId: String): SubtaskModel

    suspend fun editSubtask(requester: UserId, subtaskModel: SubtaskModel, editingScheme: SubtaskEditingScheme)
}

@Service
class SubtaskServiceImpl(
    private val subTaskCrudRepository: SubTaskCrudRepository,
    private val subtaskRepository: SubtaskRepository,
    private val subtaskEntityConverter: SuspendConverter<SubTaskEntity, SubtaskModel>,
    private val availabilityService: AvailabilityService,
) : SubtaskService {
    override suspend fun create(requester: UserId, subTaskModel: SubtaskModel): SubtaskModel {
        assert(availabilityService.availableEditSubtask(requester, subTaskModel.taskId)) { "Editing is not available" }
        return subtaskEntityConverter.convert(subTaskCrudRepository.save(subTaskModel.toEntity()))
    }

    override suspend fun getSubTasks(taskId: String): List<SubtaskModel> =
        subTaskCrudRepository.findAllByTaskId(taskId).map { subtaskEntityConverter.convert(it) }
            .toCollection(mutableListOf())

    override suspend fun getSubtask(subtaskId: String): SubtaskModel =
        subtaskEntityConverter.convert(requireNotNull(subTaskCrudRepository.findById(subtaskId)) { "" })


    @Transactional
    override suspend fun editSubtask(
        requester: UserId,
        subTaskModel: SubtaskModel,
        editingScheme: SubtaskEditingScheme
    ) {
        val subtaskId = requireNotNull(subTaskModel.id) { "SubtaskId was required" }
        if (editingScheme.executionResultEdited) {
            assert(availabilityService.availableSetExecutionResult(requester, subtaskId)) { "Editing is not allowed" }
        }
        if (editingScheme.editOnlyExecutionResult.not()) {
            assert(availabilityService.availableEditSubtask(requester, subtaskId)) { "Editing is not allowed" }
        }
        subtaskRepository.updateSubtask(subTaskModel.toEntity(), editingScheme)
            .toCollection(mutableListOf())
            .also { assert(it.size == 1) { "Was updated more or less than 1 entity" } }
    }

}

