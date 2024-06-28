package com.leijendary.extension

import com.leijendary.context.objectMapper
import java.lang.reflect.Field

fun Any.toJson(): String {
    return objectMapper.writeValueAsString(this)
}

fun Any.reflectField(property: String): Field {
    val field = try {
        javaClass.getDeclaredField(property)
    } catch (_: NoSuchFieldException) {
        javaClass.superclass.getDeclaredField(property)
    }
    field.isAccessible = true

    return field
}

fun Any.reflectGet(property: String): Any? {
    val field = reflectField(property)

    return field[this]
}

fun Any.reflectSet(property: String, value: Any?): Any? {
    val field = reflectField(property)
    field[this] = value

    return field[this]
}
