package com.leijendary.spring.boot.template.core.mapper

import com.leijendary.spring.boot.template.core.util.RequestContext.timeZone
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers.getMapper
import java.time.Instant.ofEpochMilli
import java.time.OffsetDateTime
import java.time.OffsetDateTime.ofInstant
import java.time.ZoneId

@Mapper
interface DateMapper {
    companion object {
        val INSTANCE: DateMapper = getMapper(DateMapper::class.java)
    }

    fun toOffsetDateTime(epochMillis: Long): OffsetDateTime {
        val instant = ofEpochMilli(epochMillis)
        val zoneId: ZoneId = timeZone.toZoneId()

        return ofInstant(instant, zoneId)
    }

    fun toEpochMillis(offsetDateTime: OffsetDateTime): Long {
        return offsetDateTime.toInstant().toEpochMilli()
    }
}