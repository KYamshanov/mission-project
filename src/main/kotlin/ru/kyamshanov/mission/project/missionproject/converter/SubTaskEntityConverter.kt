package ru.kyamshanov.mission.project.missionproject.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.kyamshanov.mission.project.missionproject.entity.SubTaskEntity
import ru.kyamshanov.mission.project.missionproject.entity.toModel
import ru.kyamshanov.mission.project.missionproject.models.SubtaskModel
import ru.kyamshanov.mission.project.missionproject.models.UserId
import ru.kyamshanov.mission.project.missionproject.models.UserInfo
import ru.kyamshanov.mission.project.missionproject.network.usecase.FetchUsersUseCase

@Component
internal class SubTaskEntityConverter(
    private val fetchUsersUseCase: FetchUsersUseCase
) : SuspendConverter<SubTaskEntity, SubtaskModel> {
    override suspend fun convert(source: SubTaskEntity): SubtaskModel {
        val fetchedUser = fetchUsersUseCase.fetchUser(source.responsible).getOrThrow()
        val userInfo = UserInfo(userId = fetchedUser.userId, userName = fetchedUser.userName)
        return SubtaskModel(
            taskId = source.taskId,
            title = source.title,
            description = source.text,
            createAt = source.createAt,
            startAt = source.startAt,
            endAt = source.endAt,
            responsible = userInfo,
            stage = source.stage.toModel(),
            executionResult = source.executionResult,
            id = source.id
        )
    }

}