package ru.kyamshanov.mission.project.missionproject.network.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import ru.kyamshanov.mission.project.missionproject.network.model.MappingRqDto
import ru.kyamshanov.mission.project.missionproject.network.model.MappingRsDto

interface ProfileApi {

    suspend fun mappingUsers(body: MappingRqDto): MappingRsDto
}

@Component
private class ProfileApiImpl @Autowired constructor(
    private val webClient: WebClient,
    @Value("\${PROFILE_URL}")
    private val profileUrl: String
) : ProfileApi {

    override suspend fun mappingUsers(body: MappingRqDto): MappingRsDto = withContext(Dispatchers.IO) {
        webClient.post().uri("$profileUrl/profile/internal/search/map")
            .bodyValue(body)
            .retrieve()
            .awaitBody()
    }
}