package ru.kyamshanov.mission.project.missionproject.models

data class Participant(
    val role: Role,
    val userInfo : UserInfo
) {

    enum class Role {
        PARTICIPANT, LEADER, MENTOR
    }
}
