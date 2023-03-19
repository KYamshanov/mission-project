package ru.kyamshanov.mission.project.missionproject.service

import kotlinx.coroutines.flow.toCollection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.kyamshanov.mission.project.missionproject.models.PageIndex
import ru.kyamshanov.mission.project.missionproject.models.ProjectModel
import ru.kyamshanov.mission.project.missionproject.models.ShortProjectInfo
import ru.kyamshanov.mission.project.missionproject.processor.LoadProcessor
import ru.kyamshanov.mission.project.missionproject.repository.ProjectRepository

interface SearchService {

    suspend fun findByName(name: String, pageIndex: PageIndex?): List<ProjectModel>

}

@Service
private class SearchServiceImpl @Autowired constructor(
    private val projectRepository: ProjectRepository
) : SearchService {

    override suspend fun findByName(name: String, pageIndex: PageIndex?): List<ProjectModel> =
        projectRepository.findByName(name, pageIndex).toCollection(mutableListOf())
}