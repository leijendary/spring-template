package com.leijendary.spring.core.mapper

import com.leijendary.spring.core.util.RequestContext.zoneId
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers.getMapper
import java.sql.Timestamp
import java.time.Instant
import java.time.OffsetDateTime

@Mapper
interface DateMapper {
    companion object {
        val INSTANCE: DateMapper = getMapper(DateMapper::class.java)
    }

    fun toOffsetDateTime(epochMilli: Long): OffsetDateTime {
        return Instant
            .ofEpochMilli(epochMilli)
            .atZone(zoneId)
            .toOffsetDateTime()
    }

    fun toOffsetDateTime(timestamp: Timestamp): OffsetDateTime {
        val instant = timestamp.toInstant()

        return OffsetDateTime.ofInstant(instant, zoneId)
    }

    fun toEpochMilli(offsetDateTime: OffsetDateTime): Long {
        return offsetDateTime.toInstant().toEpochMilli()
    }
}
