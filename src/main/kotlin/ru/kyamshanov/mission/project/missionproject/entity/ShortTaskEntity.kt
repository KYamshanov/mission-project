package ru.kyamshanov.mission.project.missionproject.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import ru.kyamshanov.mission.project.missionproject.models.ShortTaskModel
import ru.kyamshanov.mission.project.missionproject.models.TaskStage
import java.time.LocalDateTime

@Table("tasks")
data class ShortTaskEntity(
    @Column("project_id")
    val projectId: String,
    @Column("title")
    val title: String,
    @Column("text")
    val text: String,
    @Column("start_at")
    val startAt: LocalDateTime,
    @Column("end_at")
    val endAt: LocalDateTime,
    @Column("points")
    val points: Int,
    @Id
    @Column("id")
    private val givenId: String? = null
) : AbstractEntity(givenId)

fun ShortTaskEntity.toDomain(taskStage: TaskStage) = ShortTaskModel(
    id = id,
    title = title,
    description = text,
    taskStage = taskStage,
    startAt = startAt,
    endAt = endAt,
    points = points.takeIf { it > 0 }
)