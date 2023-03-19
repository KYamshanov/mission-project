package ru.kyamshanov.mission.project.missionproject.config

import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

/**
 * Конфигурация веб
 */
@Configuration
internal class WebConfiguration(
    private val webClientFactory: WebClientFactory
) {

    /**
     * Бин [WebClient]
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    fun webClientBean(): WebClient = webClientFactory.create()
}

@Component
internal class WebClientFactory {

    private val httpClient: HttpClient
        get() = HttpClient.create().wiretap(true)


    fun create(): WebClient {
        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .build()
    }

}