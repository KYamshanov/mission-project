package ru.kyamshanov.mission.project.missionproject.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.r2dbc.convert.MappingR2dbcConverter
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.core.flow
import org.springframework.stereotype.Repository
import ru.kyamshanov.mission.project.missionproject.entity.TaskEntity
import ru.kyamshanov.mission.project.missionproject.models.*

interface TaskRepository {

    suspend fun updateTask(taskEntity: TaskEntity, editedScheme: TaskEditingScheme): Flow<TaskEntity>
}

@Repository
private class TaskRepositoryImpl @Autowired constructor(
    private val r2dbcEntityTemplate: R2dbcEntityTemplate,
    private val converter: MappingR2dbcConverter
) : TaskRepository {

    override suspend fun updateTask(taskEntity: TaskEntity, editedScheme: TaskEditingScheme): Flow<TaskEntity> {
        val sqlQuery = buildString {
            append("UPDATE tasks SET")
            buildList {
                if (editedScheme.titleEdited) add("title" to taskEntity.title)
                if (editedScheme.descriptionEdited) add("text" to taskEntity.text)
                if (editedScheme.startAtEdited) add("start_at" to taskEntity.startAt)
                if (editedScheme.endAtEdited) add("end_at" to taskEntity.endAt)
                if (editedScheme.maxPointsEdited) add("max_points" to taskEntity.maxPoints)
                if (editedScheme.pointsEdited) add("points" to taskEntity.points)
            }.let { list ->
                list.forEachIndexed { index, s ->
                    append(" ${s.first} = '${s.second}'")
                    if (index != list.lastIndex) append(',')
                }
            }
            append(" WHERE id = '${taskEntity.id}' RETURNING *;")
        }

        return r2dbcEntityTemplate.databaseClient.sql(sqlQuery)
            .map { t, u -> converter.read(TaskEntity::class.java, t, u) }
            .flow()
    }
}