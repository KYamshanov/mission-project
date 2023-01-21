package ru.kyamshanov.mission.project.missionproject.converter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.r2dbc.postgresql.codec.Json
import org.springframework.core.convert.converter.Converter
import ru.kyamshanov.mission.project.missionproject.entity.JsonMap

/**
 * Конвертер [JsonMap] -> [Json]
 * @property objectMapper Маппер JSON
 */
class MapToJsonDbConverter(
    private val objectMapper: ObjectMapper
) : Converter<JsonMap, Json> {

    @Override
    override fun convert(source: JsonMap): Json =
        Json.of(objectMapper.writeValueAsString(source.map))
}

/**
 * Конвертер [Json] -> [JsonMap]
 * @property objectMapper Маппер JSON
 */
class DbJsonToMapConverter(
    private val objectMapper: ObjectMapper
) : Converter<Json, JsonMap> {

    @Override
    override fun convert(source: Json): JsonMap =
        JsonMap(objectMapper.readValue(source.asString()))
}