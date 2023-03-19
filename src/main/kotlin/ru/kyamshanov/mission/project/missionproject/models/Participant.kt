package ru.kyamshanov.mission.project.missionproject.models

data class Participant(
    val userId: String,
    val role: Role,
    val userName: String = ""
) {

    enum class Role {
        PARTICIPANT, LEADER
    }
}
