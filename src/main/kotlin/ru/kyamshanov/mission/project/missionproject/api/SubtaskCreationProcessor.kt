package ru.kyamshanov.mission.project.missionproject.api

import org.springframework.stereotype.Component
import ru.kyamshanov.mission.project.missionproject.dto.CreateSubTaskRqDto
import ru.kyamshanov.mission.project.missionproject.dto.CreateSubTaskRsDto
import ru.kyamshanov.mission.project.missionproject.models.SubtaskModel
import ru.kyamshanov.mission.project.missionproject.network.usecase.FetchUsersUseCase
import ru.kyamshanov.mission.project.missionproject.service.SubtaskService

interface SubtaskCreationProcessor {

    suspend fun createSubtask(request: CreateSubTaskRqDto): CreateSubTaskRsDto
}


@Component
private class SubtaskCreationProcessorImpl(
    private val fetchUsersUseCase: FetchUsersUseCase,
    private val subtaskService: SubtaskService,
) : SubtaskCreationProcessor {
    override suspend fun createSubtask(request: CreateSubTaskRqDto): CreateSubTaskRsDto {
        val foundUser = fetchUsersUseCase.fetchUser(request.responsible)

        val subtaskModel = SubtaskModel(
            taskId = request.taskId,
            title = request.title,
            description = request.description,
            startAt = request.startAt,
            endAt = request.endAt,
            responsible = foundUser.getOrThrow(),
            stage = SubtaskModel.Stage.CREATED
        )
        val createdSubtaskModel = subtaskService.create(subtaskModel)
        return CreateSubTaskRsDto(requireNotNull(createdSubtaskModel.id) { "Id cannot be null after save in db" })
    }


}