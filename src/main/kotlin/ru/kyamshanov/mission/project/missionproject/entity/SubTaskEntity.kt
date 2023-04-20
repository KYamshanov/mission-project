package ru.kyamshanov.mission.project.missionproject.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import ru.kyamshanov.mission.project.missionproject.models.SubtaskModel
import ru.kyamshanov.mission.project.missionproject.models.UserInfo
import java.time.LocalDateTime

@Table("subtasks")
data class SubTaskEntity(
    @Column("task_id")
    val taskId: String,
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
    @Column("responsible")
    val responsible: String,
    @Column("stage")
    val stage: SubtaskStage,
    @Column("execution_result")
    val executionResult: String?,
    /** Первичный ключ - Идентификатор */
    @Id
    @Column("id")
    private val givenId: String? = null
) : AbstractEntity(givenId)

fun SubtaskModel.toEntity() = SubTaskEntity(
    taskId = taskId,
    title = title,
    text = description,
    createAt = createAt,
    startAt = startAt,
    endAt = endAt,
    responsible = responsible.userId,
    stage = stage.toEntity(),
    executionResult = executionResult,
    givenId = id
)