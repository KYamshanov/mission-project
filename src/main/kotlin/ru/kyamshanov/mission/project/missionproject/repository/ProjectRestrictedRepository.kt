package ru.kyamshanov.mission.project.missionproject.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import ru.kyamshanov.mission.project.missionproject.entity.ProjectRestrictedEntity
import ru.kyamshanov.mission.project.missionproject.models.UserId

interface ProjectRestrictedRepository : CoroutineCrudRepository<ProjectRestrictedEntity, String> {


    @Query("SELECT projects.title, projects.description, projects.id, MAX(tasks.start_at) as start_at, MIN(tasks.end_at) as end_at FROM projects JOIN participants ON participants.project_id = projects.id JOIN tasks ON tasks.project_id = projects.id WHERE participants.external_user_id = :userId GROUP BY projects.id ")
    fun getUserProjects(userId: UserId): Flow<ProjectRestrictedEntity>
}