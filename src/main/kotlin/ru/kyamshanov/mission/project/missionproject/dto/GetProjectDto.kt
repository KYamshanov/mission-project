package ru.kyamshanov.mission.project.missionproject.dto

data class GetAllProjectsRqDto(
    val paginationFilter: PaginationFilter,
    val sortingFilter: SortingFilter
) {

    data class PaginationFilter(
        val page: Int,
        val size: Int
    )

    data class SortingFilter(
        val byName: Boolean
    )
}