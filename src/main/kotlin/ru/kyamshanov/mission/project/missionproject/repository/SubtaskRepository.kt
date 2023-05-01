package ru.kyamshanov.mission.project.missionproject.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.r2dbc.convert.MappingR2dbcConverter
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.core.flow
import org.springframework.stereotype.Repository
import ru.kyamshanov.mission.project.missionproject.entity.ProjectEntity
import ru.kyamshanov.mission.project.missionproject.entity.SubTaskEntity
import ru.kyamshanov.mission.project.missionproject.models.PageIndex
import ru.kyamshanov.mission.project.missionproject.models.ProjectEditingScheme
import ru.kyamshanov.mission.project.missionproject.models.ProjectModel
import ru.kyamshanov.mission.project.missionproject.models.SubtaskEditingScheme

interface SubtaskRepository {

    fun updateSubtask(subtaskEntity: SubTaskEntity, editedScheme: SubtaskEditingScheme): Flow<SubTaskEntity>
}

@Repository
private class SubtaskRepositoryImpl @Autowired constructor(
    private val r2dbcEntityTemplate: R2dbcEntityTemplate,
    private val converter: MappingR2dbcConverter
) : SubtaskRepository {

    override fun updateSubtask(
        subtaskEntity: SubTaskEntity,
        editedScheme: SubtaskEditingScheme
    ): Flow<SubTaskEntity> {
        val sqlQuery = buildString {
            append("UPDATE subtasks SET")
            buildList {
                if (editedScheme.titleEdited) add("title" to subtaskEntity.title)
                if (editedScheme.descriptionEdited) add("text" to subtaskEntity.text)
                if (editedScheme.responsibleEdited) add("responsible" to subtaskEntity.responsible)
                if (editedScheme.stateEdited) add("stage" to subtaskEntity.stage)
                if (editedScheme.executionResultEdited) add("execution_result" to subtaskEntity.executionResult)
            }.let { list ->
                list.forEachIndexed { index, s ->
                    append(" ${s.first} = '${s.second}'")
                    if (index != list.lastIndex) append(',')
                }
            }
            append(" WHERE id = '${subtaskEntity.id}' RETURNING *;")
        }

        return r2dbcEntityTemplate.databaseClient.sql(sqlQuery)
            .map { t, u -> converter.read(SubTaskEntity::class.java, t, u) }
            .flow()
    }
}