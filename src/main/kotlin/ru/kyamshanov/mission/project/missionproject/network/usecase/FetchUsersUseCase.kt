package ru.kyamshanov.mission.project.missionproject.network.usecase

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.kyamshanov.mission.project.missionproject.models.Team
import ru.kyamshanov.mission.project.missionproject.network.api.ProfileApi


interface FetchUsersUseCase {

    suspend fun fetchUserNames(team: Team): Result<Team>
}

@Component
class FetchUsersUseCaseImpl @Autowired constructor(
    private val profileApi: ProfileApi
) : FetchUsersUseCase {
    override suspend fun fetchUserNames(team: Team): Result<Team> = runCatching {
        val users = profileApi.mappingUsers(team.participants.map { it.userId }).users
        team.copy(
            participants = team.participants.map { participant ->
                participant.copy(userName = users.find { it.id == participant.userId }?.name.orEmpty())
            })
    }

}