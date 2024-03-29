package ru.kyamshanov.mission.project.missionproject.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.r2dbc.convert.MappingR2dbcConverter
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.core.flow
import org.springframework.stereotype.Repository
import ru.kyamshanov.mission.project.missionproject.entity.ProjectEntity
import ru.kyamshanov.mission.project.missionproject.models.PageIndex
import ru.kyamshanov.mission.project.missionproject.models.ProjectEditingScheme
import ru.kyamshanov.mission.project.missionproject.models.ProjectModel
import ru.kyamshanov.mission.project.missionproject.models.UserId

interface ProjectRepository {

    suspend fun findByName(name: String, pageIndex: PageIndex? = null): Flow<ProjectModel>

    suspend fun updateProject(projectModel: ProjectEntity, editedScheme: ProjectEditingScheme): Flow<ProjectEntity>
}

/**
 * Репозиторий используеющий нативные запросы к БД
 * @property r2dbcEntityTemplate Реактивное средство для запросов к БД
 * @property converter Средство для конвертирования записей из БД в необходимую модель Entity
 */
@Repository
private class ProjectRepositoryImpl @Autowired constructor(
    private val r2dbcEntityTemplate: R2dbcEntityTemplate,
    private val converter: MappingR2dbcConverter
) : ProjectRepository {

    override suspend fun findByName(name: String, pageIndex: PageIndex?): Flow<ProjectModel> {
        val sqlQuery = buildString {
            append("SELECT * FROM projects WHERE title ~ '${name}' ")
            pageIndex?.let { "LIMIT ${it.pageSize} OFFSET ${it.page * it.pageSize}" }
            append(";")
        }

        return r2dbcEntityTemplate.databaseClient.sql(sqlQuery)
            .map { t, u -> converter.read(ProjectModel::class.java, t, u) }
            .flow()
    }

    override suspend fun updateProject(
        projectModel: ProjectEntity,
        editedScheme: ProjectEditingScheme
    ): Flow<ProjectEntity> {
        val sqlQuery = buildString {
            append("UPDATE projects SET")
            buildList {
                if (editedScheme.titleEdited) add("title" to projectModel.title)
                if (editedScheme.descriptionEdited) add("description" to projectModel.description)
            }.let { list ->
                list.forEachIndexed { index, s ->
                    append(" ${s.first} = '${s.second}'")
                    if (index != list.lastIndex) append(',')
                }
            }
            append(" WHERE id = '${projectModel.id}' RETURNING *;")
        }

        return r2dbcEntityTemplate.databaseClient.sql(sqlQuery)
            .map { t, u -> converter.read(ProjectEntity::class.java, t, u) }
            .flow()
    }
}