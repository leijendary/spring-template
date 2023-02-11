package com.leijendary.spring.template.core.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StringDeserializer
import org.springframework.boot.jackson.JsonComponent

private val exclusions = arrayOf("currentPassword", "password")

@JsonComponent
class StringTrimDeserializer : StringDeserializer() {
    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): String? {
        // Get the result from the original StringDeserializer
        val deserialized = super.deserialize(jsonParser, deserializationContext)

        // But skip the whitelisted fields
        if (exclusions.contains(jsonParser.currentName())) {
            return deserialized
        }

        // ... and just trim it
        return deserialized?.let { obj -> obj.trim { it <= ' ' } }
    }
}
