package com.leijendary.spring.boot.template.core.extension

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.leijendary.spring.boot.template.core.util.RequestContext.path
import com.leijendary.spring.boot.template.core.util.SpringContext.Companion.getBean
import java.lang.reflect.Field

private val mapper: ObjectMapper = getBean(ObjectMapper::class.java)

fun Any.toLocation(): String {
    return path?.let { if (!it.endsWith("/")) "$path/$this" else "$path$this" } ?: ""
}

fun <T> Any.toClass(type: Class<T>): T {
    return mapper.convertValue(this, type)
}

fun Any.reflectField(property: String): Field {
    val field = this.javaClass.getDeclaredField(property)
    field.isAccessible = true

    return field
}

fun Any.reflectGet(property: String): Any? {
    val field = reflectField(property)

    return field.get(this)
}

fun Any.reflectSet(property: String, value: Any?): Any? {
    val field = reflectField(property)

    field.set(this, value)

    return field.get(this)
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