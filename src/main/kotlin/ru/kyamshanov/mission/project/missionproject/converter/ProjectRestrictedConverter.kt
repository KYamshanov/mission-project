package ru.kyamshanov.mission.project.missionproject.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.kyamshanov.mission.project.missionproject.entity.ProjectRestrictedEntity
import ru.kyamshanov.mission.project.missionproject.models.ProjectRestrictedModel
import ru.kyamshanov.mission.project.missionproject.usecase.GetTaskStageUseCase

@Component
class ProjectRestrictedConverter(
    private val getTaskStageUseCase: GetTaskStageUseCase
) : Converter<ProjectRestrictedEntity, ProjectRestrictedModel> {
    override fun convert(source: ProjectRestrictedEntity): ProjectRestrictedModel {
        val taskStage = getTaskStageUseCase(source.startAt, source.endAt)
        return ProjectRestrictedModel(
            projectId = source.projectId,
            title = source.title,
            description = source.description,
            startAt = source.startAt,
            endAt = source.endAt,
            stage = taskStage
        )
    }
}