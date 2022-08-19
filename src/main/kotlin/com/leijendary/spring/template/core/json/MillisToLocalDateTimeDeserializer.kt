package com.leijendary.spring.template.core.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_INT
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.leijendary.spring.template.core.mapper.DateMapper
import org.springframework.boot.jackson.JsonComponent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME


@JsonComponent
class MillisToLocalDateTimeDeserializer : LocalDateTimeDeserializer(ISO_LOCAL_DATE_TIME) {
    companion object {
        private val MAPPER = DateMapper.INSTANCE
    }

    override fun deserialize(parser: JsonParser, context: DeserializationContext): LocalDateTime {
        if (parser.hasToken(VALUE_NUMBER_INT)) {
            return MAPPER.toLocalDateTime(parser.valueAsLong)
        }

        return super.deserialize(parser, context)
    }
}