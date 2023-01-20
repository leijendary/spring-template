package com.leijendary.spring.template.core.extension

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.leijendary.spring.template.core.util.SpringContext.Companion.getBean
import java.lang.reflect.Field
import kotlin.reflect.KClass

private val mapper = getBean(ObjectMapper::class)

fun <T : Any> Any.toClass(type: KClass<T>): T {
    return mapper.convertValue(this, type.java)
}

fun Any.reflectField(property: String): Field {
    val field = try {
        this.javaClass.getDeclaredField(property)
    } catch (_: NoSuchFieldException) {
        this.javaClass.superclass.getDeclaredField(property)
    }
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
