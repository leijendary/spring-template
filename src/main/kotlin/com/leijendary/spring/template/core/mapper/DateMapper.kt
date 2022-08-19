package com.leijendary.spring.template.core.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers.getMapper
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC

@Mapper
interface DateMapper {
    companion object {
        val INSTANCE: DateMapper = getMapper(DateMapper::class.java)
    }

    fun toLocalDateTime(epochMillis: Long): LocalDateTime {
        val instant = Instant.ofEpochMilli(epochMillis)

        return LocalDateTime.ofInstant(instant, UTC)
    }

    fun toEpochMillis(localDateTime: LocalDateTime): Long {
        return localDateTime
            .toInstant(UTC)
            .toEpochMilli()
    }
}