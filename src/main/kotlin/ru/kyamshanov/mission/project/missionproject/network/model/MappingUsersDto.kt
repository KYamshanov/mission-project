package ru.kyamshanov.mission.project.missionproject.network.model

import ru.kyamshanov.mission.project.missionproject.models.UserInfo


typealias MappingRqDto = List<String>

data class MappingRsDto(
    val users: List<UserInfo>
) {

    data class UserInfo(
        val id: String,
        val firstname: String?,
        val lastname: String?,
        val patronymic: String?,
        val group: String?
    )
}