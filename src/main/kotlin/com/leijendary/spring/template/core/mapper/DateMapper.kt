package com.leijendary.spring.template.core.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers.getMapper
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset.UTC

@Mapper
interface DateMapper {
    companion object {
        val INSTANCE: DateMapper = getMapper(DateMapper::class.java)
    }

    fun toOffsetDateTime(epochMilli: Long): OffsetDateTime = Instant.ofEpochMilli(epochMilli).atOffset(UTC)

    fun toEpochMilli(offsetDateTime: OffsetDateTime): Long = offsetDateTime.toInstant().toEpochMilli()
}
