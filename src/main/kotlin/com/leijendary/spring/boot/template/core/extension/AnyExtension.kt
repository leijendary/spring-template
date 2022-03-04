package com.leijendary.spring.boot.template.core.extension

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.leijendary.spring.boot.template.core.util.RequestContext.path
import com.leijendary.spring.boot.template.core.util.SpringContext.Companion.getBean

private val mapper: ObjectMapper = getBean(ObjectMapper::class.java)

fun Any.toLocation(): String {
    return path?.let { if (!it.endsWith("/")) "$path/$this" else "$path$this" } ?: ""
}

fun <T> Any.toClass(type: Class<T>): T {
    return mapper.convertValue(this, type)
}

object AnyUtil {
    private val log = logger()

    fun Any.toJson(): String? {
        try {
            return mapper.writeValueAsString(this)
        } catch (e: JsonProcessingException) {
            log.warn("Failed to parse object to json", e)
        }

        return null
    }
}