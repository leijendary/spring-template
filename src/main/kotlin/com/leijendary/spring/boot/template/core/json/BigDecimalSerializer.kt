package com.leijendary.spring.boot.template.core.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.leijendary.spring.boot.template.core.config.properties.NumberProperties
import org.springframework.boot.jackson.JsonComponent
import java.math.BigDecimal

@JsonComponent
class BigDecimalSerializer(private val numberProperties: NumberProperties) : JsonSerializer<BigDecimal>() {

    override fun serialize(value: BigDecimal?, generator: JsonGenerator, provider: SerializerProvider) {
        val scale = numberProperties.scale
        val round = numberProperties.round
        val formatted = value?.setScale(scale, round)?.toPlainString()

        generator.writeNumber(formatted)
    }
}