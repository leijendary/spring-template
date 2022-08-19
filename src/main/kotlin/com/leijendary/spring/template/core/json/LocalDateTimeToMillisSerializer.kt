package com.leijendary.spring.template.core.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.leijendary.spring.template.core.mapper.DateMapper
import org.springframework.boot.jackson.JsonComponent
import java.time.LocalDateTime

@JsonComponent
class LocalDateTimeToMillisSerializer(t: Class<LocalDateTime?>?) : StdSerializer<LocalDateTime>(t) {
    companion object {
        private val MAPPER = DateMapper.INSTANCE
    }

    override fun serialize(localDateTime: LocalDateTime, generator: JsonGenerator, provider: SerializerProvider) {
        val epochMillis = MAPPER.toEpochMillis(localDateTime)

        generator.writeNumber(epochMillis)
    }
}