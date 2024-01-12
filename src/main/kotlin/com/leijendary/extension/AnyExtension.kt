package com.leijendary.extension

import com.leijendary.util.BeanContainer.objectMapper
import java.io.Serializable
import java.lang.reflect.Field

fun <T : Serializable> T.toJson(): String {
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

fun Any.reflectGet(property: String): Any? = reflectField(property).get(this)

fun Any.reflectSet(property: String, value: Any?): Any? {
    val field = reflectField(property)
    field[this] = value

    return field[this]
}
