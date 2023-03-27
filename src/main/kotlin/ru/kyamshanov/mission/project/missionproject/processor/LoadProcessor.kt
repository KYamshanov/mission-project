package ru.kyamshanov.mission.project.missionproject.processor

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.kyamshanov.mission.project.missionproject.dto.GetAllProjectsRqDto
import ru.kyamshanov.mission.project.missionproject.dto.GetAllProjectsRsDto
import ru.kyamshanov.mission.project.missionproject.dto.toShortProjectInfoDto
import ru.kyamshanov.mission.project.missionproject.models.PageIndex
import ru.kyamshanov.mission.project.missionproject.service.SearchService

interface LoadProcessor {

    suspend fun searchByName(request: GetAllProjectsRqDto): GetAllProjectsRsDto
}

@Component
private class LoadProcessorImpl @Autowired constructor(
    private val searchService: SearchService
) : LoadProcessor {
    override suspend fun searchByName(request: GetAllProjectsRqDto): GetAllProjectsRsDto {
        val pageIndex = request.pageIndex?.let { PageIndex(it.page, it.size) }
        val projects = searchService.findByName(request.filter.name, pageIndex)
        return GetAllProjectsRsDto(projects.map { it.toShortProjectInfoDto() })
    }

}