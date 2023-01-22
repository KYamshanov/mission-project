package ru.kyamshanov.mission.project.missionproject.dto

import com.fasterxml.jackson.annotation.JsonFormat
import ru.kyamshanov.mission.project.missionproject.models.ProjectStage
import java.time.LocalDateTime

data class GetAllProjectsRqDto(
    val paginationFilter: PaginationFilter,
    val
) {

    data class PaginationFilter(
        val page: Int,
        val size: Int
    )

    data class SortingFilter(
        val byName : Boolean,
        val by
    )
}