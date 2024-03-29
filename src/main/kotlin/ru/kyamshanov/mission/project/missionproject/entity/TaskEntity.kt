package ru.kyamshanov.mission.project.missionproject.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("tasks")
data class TaskEntity(
    @Column("project_id")
    val projectId: String,
    @Column("title")
    val title: String,
    @Column("text")
    val text: String,
    @Column("create_at")
    val createAt: LocalDateTime,
    @Column("start_at")
    val startAt: LocalDateTime,
    @Column("end_at")
    val endAt: LocalDateTime,
    @Column("max_points")
    val maxPoints: Int,
    @Column("points")
    val points: Int,
    /** Первичный ключ - Идентификатор */
    @Id
    @Column("id")
    private val givenId: String? = null
) : AbstractEntity(givenId)