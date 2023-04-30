package ru.kyamshanov.mission.project.missionproject.entity

import ru.kyamshanov.mission.project.missionproject.models.*


/* ---Model to Entity ---- */

fun ProjectModel.toEntity() = ProjectEntity(
    title = title,
    description = description,
    givenId = id
)

fun Participant.toEntity(projectId: String, id: String? = null) = ParticipantEntity(
    projectId = projectId,
    userId = userInfo.userId,
    role = role.toEntity(),
    givenId = id
)

fun Participant.Role.toEntity(): ParticipantRole? = when (this) {
    Participant.Role.PARTICIPANT -> null
    Participant.Role.LEADER -> ParticipantRole.LEADER
    Participant.Role.MENTOR -> ParticipantRole.MENTOR
}

fun ProjectStage.Stage.toType(): ProjectStageType = when (this) {
    ProjectStage.Stage.CREATED -> ProjectStageType.CREATED
    ProjectStage.Stage.CANCELED -> ProjectStageType.CANCELED
    ProjectStage.Stage.PREPARING -> ProjectStageType.PREPARING
    ProjectStage.Stage.RESEARCH -> ProjectStageType.RESEARCH
    ProjectStage.Stage.DEVELOP -> ProjectStageType.DEVELOP
    ProjectStage.Stage.FINISHING -> ProjectStageType.FINISHING
}

fun ProjectStage.toEntity(projectId: String) = StageHistoryEntity(
    projectId = projectId,
    stage = stage.toType(),
    updatedAt = createdAt
)

fun TaskModel.toEntity() = TaskEntity(
    projectId = projectId,
    title = title,
    createAt = createAt,
    text = text,
    givenId = id,
    startAt = startAt,
    endAt = endAt,
    maxPoints = maxPoints,
    points = points
)

/* ---- Entity to Model ---- */

fun ParticipantEntity.toModel() = Participant(
    role = role.toModel(),
    userInfo = UserInfo(userId = userId, userName = "")
)


fun ProjectEntity.toModel() = ProjectModel(
    title = title,
    description = description,
    id = id
)

fun ProjectEntity.toCreatedProjectInfo() = ShortProjectInfo(
    id = id,
    title = title
)

fun ParticipantRole?.toModel(): Participant.Role = when (this) {
    ParticipantRole.LEADER -> Participant.Role.LEADER
    ParticipantRole.MENTOR -> Participant.Role.MENTOR
    null -> Participant.Role.PARTICIPANT
}

fun ProjectStageType.toModel(): ProjectStage.Stage = when (this) {
    ProjectStageType.CREATED -> ProjectStage.Stage.CREATED
    ProjectStageType.CANCELED -> ProjectStage.Stage.CANCELED
    ProjectStageType.PREPARING -> ProjectStage.Stage.PREPARING
    ProjectStageType.RESEARCH -> ProjectStage.Stage.RESEARCH
    ProjectStageType.DEVELOP -> ProjectStage.Stage.DEVELOP
    ProjectStageType.FINISHING -> ProjectStage.Stage.FINISHING
}

fun StageHistoryEntity.toModel() = ProjectStage(
    stage = stage.toModel(),
    createdAt = updatedAt
)

fun TaskEntity.toModel(stage: TaskStage) = TaskModel(
    id = id,
    projectId = projectId,
    title = title,
    text = text,
    createAt = createAt,
    stage = stage,
    startAt = startAt,
    endAt = endAt,
    maxPoints = maxPoints,
    points = points
)

fun LightTaskEntity.toModel() = LightTaskModel(
    id = id,
    title = title
)