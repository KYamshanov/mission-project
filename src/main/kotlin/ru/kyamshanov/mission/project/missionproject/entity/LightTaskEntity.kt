package ru.kyamshanov.mission.project.missionproject.entity

import org.springframework.data.relational.core.mapping.Column
import java.time.LocalDateTime

data class LightTaskEntity(
    @Column("title")
    val title: String,
    @Column("create_at")
    val createAt: LocalDateTime,
    @Column("id")
    val id: String
)