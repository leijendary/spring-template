package com.leijendary.spring.boot.template.core.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.leijendary.spring.boot.template.core.util.RequestContext.zoneId
import org.springframework.boot.jackson.JsonComponent
import java.time.Instant.ofEpochMilli
import java.time.OffsetDateTime
import java.time.OffsetDateTime.ofInstant

@JsonComponent
class MillisToOffsetDeserializer(t: Class<Long?>?) : StdDeserializer<OffsetDateTime>(t) {
    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): OffsetDateTime {
        val millis = jsonParser.valueAsLong
        val instant = ofEpochMilli(millis)

        return ofInstant(instant, zoneId)
    }
}