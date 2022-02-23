package com.leijendary.spring.boot.template.core.util

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.leijendary.spring.boot.template.core.util.RequestContext.path
import com.leijendary.spring.boot.template.core.util.SpringContext.Companion.getBean


fun Any.toLocation(): String {
    return path?.let { if (!it.endsWith("/")) "$path/$this" else "$path$this" } ?: ""
}

object AnyUtil {
    private val log = logger()
    private val mapper: ObjectMapper = getBean(ObjectMapper::class.java)


    fun <T> Any.toClass(type: Class<T>): T {
        return mapper.convertValue(this, type)
    }

    fun Any.toJson(): String? {
        try {
            return mapper.writeValueAsString(this)
        } catch (e: JsonProcessingException) {
            log.warn("Failed to parse object to json", e)
        }

        return null
    }

    @Throws(JsonProcessingException::class)
    fun <T> String.toClass(type: Class<T>): T {
        return mapper.readValue(this, type)
    }
}