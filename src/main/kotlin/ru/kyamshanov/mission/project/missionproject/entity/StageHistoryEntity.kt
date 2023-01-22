package ru.kyamshanov.mission.project.missionproject.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("stage_history")
data class StageHistoryEntity(
    @Column("project_id")
    val projectId: String,
    @Column("stage")
    val stage: ProjectStageType,
    @Column("updatedAt")
    val updatedAt: LocalDateTime,
    /** Первичный ключ - Идентификатор */
    @Id
    @Column("id")
    private val givenId: String? = null
) : AbstractEntity(givenId)