package com.leijendary.spring.template.core.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.leijendary.spring.template.core.data.PageResponse
import org.springframework.boot.jackson.JsonComponent
import org.springframework.data.domain.Page

@JsonComponent
class PageSerializer : JsonSerializer<Page<Any>>() {
    override fun serialize(page: Page<Any>, generator: JsonGenerator, provider: SerializerProvider) {
        val response = PageResponse(page)

        generator.writeObject(response)
    }
}