package ru.kyamshanov.mission.project.missionproject.entity

import org.springframework.data.relational.core.mapping.Column
import java.time.LocalDateTime

data class ProjectRestrictedEntity(
    @Column("title")
    val title: String,
    @Column("description")
    val description: String,
    /** Первичный ключ - Идентификатор */
    @Column("id")
    val projectId: String,
    @Column("start_at")
    val startAt: LocalDateTime,
    @Column("end_at")
    val endAt: LocalDateTime,
)