package ru.kyamshanov.mission.project.missionproject.network.model


typealias MappingRqDto = List<String>

data class MappingRsDto(
    val users: List<UserInfo>
) {

    data class UserInfo(
        val id: String,
        val name: String?,
        val age: Int?
    )
}