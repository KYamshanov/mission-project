package ru.kyamshanov.mission.project.missionproject.converter

import org.springframework.data.r2dbc.convert.EnumWriteSupport
import ru.kyamshanov.mission.project.missionproject.entity.ParticipantRole
import ru.kyamshanov.mission.project.missionproject.entity.ProjectStageType

internal class ParticipantRoleConverter : EnumWriteSupport<ParticipantRole>()

internal class ProjectStageConverter : EnumWriteSupport<ProjectStageType>()