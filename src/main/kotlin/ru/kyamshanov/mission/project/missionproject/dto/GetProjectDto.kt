package ru.kyamshanov.mission.project.missionproject.dto

data class GetAllProjectsRqDto(
    val pageIndex: PaginationFilter? = null,
    val filter: SortingFilter
) {

    data class PaginationFilter(
        val page: Int,
        val size: Int
    )

    data class SortingFilter(
        val name: String
    )
}

data class GetAllProjectsRsDto(
    val projects: List<ShortProjectInfoDto>
)