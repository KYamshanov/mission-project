package ru.kyamshanov.mission.project.missionproject.dto

import ru.kyamshanov.mission.project.missionproject.models.UserInfo

data class UserInfoDto(
    val userId: String,
    val userName: String
)

fun UserInfo.toDto() = UserInfoDto(
    userId = userId,
    userName = userName,
)