package com.leijendary.spring.boot.template.core.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.io.IOException
import java.time.OffsetDateTime

open class OffsetDateTimeToEpochMillisSerializer protected constructor(t: Class<OffsetDateTime?>?) :
    StdSerializer<OffsetDateTime>(t) {
    constructor() : this(null)

    @Throws(IOException::class)
    override fun serialize(offsetDateTime: OffsetDateTime, generator: JsonGenerator, provider: SerializerProvider) {
        val epochMillis = offsetDateTime.toInstant().toEpochMilli()

        generator.writeNumber(epochMillis)
    }
}