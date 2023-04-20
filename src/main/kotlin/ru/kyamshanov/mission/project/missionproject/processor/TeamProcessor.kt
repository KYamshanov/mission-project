package ru.kyamshanov.mission.project.missionproject.processor

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.kyamshanov.mission.project.missionproject.exception.PermissionsException
import ru.kyamshanov.mission.project.missionproject.models.Team
import ru.kyamshanov.mission.project.missionproject.models.UserId
import ru.kyamshanov.mission.project.missionproject.service.TeamService

interface TeamProcessor {

    suspend fun getTeam(requesting: UserId, projectId: String): Team
}

@Component
private class TeamProcessorImpl @Autowired constructor(
    private val teamService: TeamService
) : TeamProcessor {
    override suspend fun getTeam(requesting: UserId, projectId: String): Team {
        val team = teamService.getTeam(projectId)
        if (team.participants.find { it.userInfo.userId == requesting } == null) throw PermissionsException("Found team has not requesting user")
        return team
    }

}
