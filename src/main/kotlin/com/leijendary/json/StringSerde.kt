package com.leijendary.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class StringTrimSerde : JsonSerializer<String>() {
    override fun serialize(value: String, generator: JsonGenerator, provider: SerializerProvider) {
        val value = value.trim()

        generator.writeString(value)
    }
}
