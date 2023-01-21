package ru.kyamshanov.mission.project.missionproject.entity

import ru.kyamshanov.mission.project.missionproject.models.Participant
import ru.kyamshanov.mission.project.missionproject.models.ProjectModel


/* ---Model to Entity ---- */

fun ProjectModel.toEntity() = ProjectEntity(
    title = title,
    description = description,
    givenId = id
)

fun Participant.toEntity(projectId: String, id: String? = null) = ParticipantEntity(
    projectId = projectId,
    userId = userId,
    role = role.toEntity(),
    givenId = id
)

fun Participant.Role.toEntity(): ParticipantRole? = when (this) {
    Participant.Role.PARTICIPANT -> null
    Participant.Role.LEADER -> ParticipantRole.LEADER
}

/* ---- Entity to Model ---- */

fun ParticipantEntity.toModel() = Participant(
    userId = userId,
    role = role.toModel()
)


fun ProjectEntity.toModel() = ProjectModel(
    title = title,
    description = description,
    id = id
)

fun ParticipantRole?.toModel(): Participant.Role = when (this) {
    ParticipantRole.LEADER -> Participant.Role.LEADER
    null -> Participant.Role.PARTICIPANT
}
