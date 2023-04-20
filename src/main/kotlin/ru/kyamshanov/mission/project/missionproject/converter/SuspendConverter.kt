package ru.kyamshanov.mission.project.missionproject.converter

interface SuspendConverter<Source : Any, Target : Any> {

    suspend fun convert(source: Source): Target
}