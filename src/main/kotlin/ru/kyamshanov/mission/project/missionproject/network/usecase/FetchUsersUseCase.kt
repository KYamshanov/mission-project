package ru.kyamshanov.mission.project.missionproject.network.usecase

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.kyamshanov.mission.project.missionproject.models.Team
import ru.kyamshanov.mission.project.missionproject.models.UserId
import ru.kyamshanov.mission.project.missionproject.models.UserInfo
import ru.kyamshanov.mission.project.missionproject.network.api.ProfileApi
import ru.kyamshanov.mission.project.missionproject.network.converter.UserInfoConverter


interface FetchUsersUseCase {

    suspend fun fetchUserNames(team: Team): Result<Team>

    suspend fun fetchUser(userId: UserId): Result<UserInfo>
}

@Component
class FetchUsersUseCaseImpl @Autowired constructor(
    private val profileApi: ProfileApi,
    private val userInfoConverter: UserInfoConverter
) : FetchUsersUseCase {
    override suspend fun fetchUserNames(team: Team): Result<Team> = runCatching {
        val users = profileApi.mappingUsers(team.participants.map { it.userInfo.userId }).users
        team.copy(
            participants = team.participants.map { participant ->
                participant.copy(
                    userInfo = users.find { it.id == participant.userInfo.userId }
                        ?.let { userInfoConverter.convert(it) } ?: participant.userInfo
                )
            })
    }

    override suspend fun fetchUser(userId: UserId): Result<UserInfo> = runCatching {
        val foundUser = profileApi.mappingUsers(listOf(userId)).users
            .also { if (it.size != 1) throw IllegalStateException("not one user was found ${it.size}") }[0]
        userInfoConverter.convert(foundUser)
    }
}