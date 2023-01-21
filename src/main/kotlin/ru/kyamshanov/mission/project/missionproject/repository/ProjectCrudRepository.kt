package ru.kyamshanov.mission.project.missionproject.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import ru.kyamshanov.mission.project.missionproject.entity.ProjectEntity

interface ProjectCrudRepository : CoroutineCrudRepository<ProjectEntity, String>