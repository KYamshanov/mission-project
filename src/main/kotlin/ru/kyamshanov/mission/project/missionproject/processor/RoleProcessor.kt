package ru.kyamshanov.mission.project.missionproject.processor

import org.springframework.stereotype.Component
import ru.kyamshanov.mission.project.missionproject.models.Participant
import ru.kyamshanov.mission.project.missionproject.models.UserId
import ru.kyamshanov.mission.project.missionproject.models.UserInfo
import ru.kyamshanov.mission.project.missionproject.service.TeamService

interface RoleProcessor {

    suspend fun setRole(projectId: String, userId: UserId, role: Participant.Role)
}

@Component
class RoleProcessorImpl(
    private val teamService: TeamService
) : RoleProcessor {

    override suspend fun setRole(projectId: String, userId: UserId, role: Participant.Role) {
        when (role) {
            Participant.Role.PARTICIPANT -> teamService.setRole(
                projectId = projectId,
                participant = Participant(role = Participant.Role.PARTICIPANT, userInfo = UserInfo(userId, "")),
                role = role
            )

            Participant.Role.LEADER -> teamService.setLeader(
                projectId = projectId,
                participant = Participant(role = Participant.Role.PARTICIPANT, userInfo = UserInfo(userId, "")),
            )

            Participant.Role.MENTOR -> teamService.setMentor(
                projectId = projectId,
                participant = Participant(role = Participant.Role.PARTICIPANT, userInfo = UserInfo(userId, "")),
            )
        }
    }


}