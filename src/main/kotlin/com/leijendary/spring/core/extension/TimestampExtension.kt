package com.leijendary.spring.core.extension

import com.leijendary.spring.core.util.RequestContext.zoneId
import java.sql.Timestamp
import java.time.OffsetDateTime

fun Timestamp.toOffsetDateTime(): OffsetDateTime {
    val instant = toInstant()

    return OffsetDateTime.ofInstant(instant, zoneId)
}
