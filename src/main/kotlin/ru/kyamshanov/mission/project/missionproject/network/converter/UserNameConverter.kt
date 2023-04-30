package ru.kyamshanov.mission.project.missionproject.network.converter

import org.springframework.stereotype.Component
import ru.kyamshanov.mission.project.missionproject.models.UserInfo
import ru.kyamshanov.mission.project.missionproject.network.model.MappingRsDto

@Component
class UserInfoConverter {

    fun convert(source: MappingRsDto.UserInfo): UserInfo = UserInfo(
        userId = source.id,
        userName = nameFormat(source)
    )

    private fun nameFormat(info: MappingRsDto.UserInfo) = buildString {
        append(info.firstname ?: "Аноним")
        info.lastname?.let { name ->
            append(' ')
            append(name[0])
            append('.')
        }

        info.patronymic?.let { name ->
            append(' ')
            append(name[0])
            append('.')
        }

        info.group?.let { group ->
            append(' ')
            append(group)
        }
    }
}