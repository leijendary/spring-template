package com.leijendary.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class StringDeserializer : JsonDeserializer<String>() {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): String? {
        return parser.text?.trim()
    }
}
