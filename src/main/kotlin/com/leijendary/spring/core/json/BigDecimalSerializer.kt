package com.leijendary.spring.core.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.leijendary.spring.core.extension.scaled
import org.springframework.boot.jackson.JsonComponent
import java.math.BigDecimal

@JsonComponent
class BigDecimalSerializer : JsonSerializer<BigDecimal>() {
    override fun serialize(value: BigDecimal?, generator: JsonGenerator, provider: SerializerProvider) {
        val formatted = value?.scaled()

        generator.writeNumber(formatted)
    }
}
