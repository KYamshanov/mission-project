package ru.kyamshanov.mission.project.missionproject.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("participants")
data class ParticipantEntity(

    @Column("project_id")
    val projectId: String,
    @Column("external_user_id")
    val userId: String,

    /** Первичный ключ - Идентификатор */
    @Id
    @Column("id")
    private val givenId: String? = null
) : AbstractEntity(givenId)