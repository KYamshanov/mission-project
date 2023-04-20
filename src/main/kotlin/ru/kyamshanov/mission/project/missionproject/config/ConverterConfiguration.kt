package ru.kyamshanov.mission.project.missionproject.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.r2dbc.postgresql.codec.Json
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import ru.kyamshanov.mission.project.missionproject.converter.*
import ru.kyamshanov.mission.project.missionproject.converter.ParticipantRoleConverter
import ru.kyamshanov.mission.project.missionproject.converter.ProjectStageConverter
import ru.kyamshanov.mission.project.missionproject.converter.SubtaskStageConverter
import ru.kyamshanov.mission.project.missionproject.entity.JsonMap
import ru.kyamshanov.mission.project.missionproject.entity.ParticipantRole
import ru.kyamshanov.mission.project.missionproject.entity.ProjectStageType
import ru.kyamshanov.mission.project.missionproject.entity.SubtaskStage


/**
 * Конфигурация конвертеров
 */
@Configuration
class ConverterConfiguration {

    @Bean
    fun objectMapper(): ObjectMapper = jacksonObjectMapper().apply {
        registerModule(JavaTimeModule())
    }

    @Bean
    fun mapToJsonStringConverter(objectMapper: ObjectMapper): Converter<JsonMap, Json> =
        MapToJsonDbConverter(objectMapper)

    @Bean
    fun jsonStringToMapConverter(objectMapper: ObjectMapper): Converter<Json, JsonMap> =
        DbJsonToMapConverter(objectMapper)

    @Bean
    fun participantRoleConverter(): Converter<ParticipantRole, ParticipantRole> =
        ParticipantRoleConverter()

    @Bean
    fun projectStageConverter(): Converter<ProjectStageType, ProjectStageType> =
        ProjectStageConverter()

    @Bean
    fun subtaskStageConverter(): Converter<SubtaskStage, SubtaskStage> = SubtaskStageConverter()
}