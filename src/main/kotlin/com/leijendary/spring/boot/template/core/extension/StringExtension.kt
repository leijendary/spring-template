package com.leijendary.spring.boot.template.core.extension

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.leijendary.spring.boot.template.core.util.SpringContext
import java.lang.Character.toLowerCase
import java.lang.Character.toUpperCase

private val mapper: ObjectMapper = SpringContext.getBean(ObjectMapper::class.java)

@Throws(JsonProcessingException::class)
fun <T> String.toClass(type: Class<T>): T {
    return mapper.readValue(this, type)
}

fun String.snakeCaseToCamelCase(capitalizeFirst: Boolean = false): String {
    val builder = StringBuilder()
    val strings = this.split("_".toRegex()).toTypedArray()
    strings.forEach { builder.append(it.upperCaseFirst()) }

    val result = builder.toString()

    return if (!capitalizeFirst) result.lowerCaseFirst() else result
}

fun String.upperCaseFirst(): String {
    val chars = this.toCharArray()
    chars[0] = toUpperCase(chars[0])

    return String(chars)
}

fun String.lowerCaseFirst(): String {
    val chars = this.toCharArray()
    chars[0] = toLowerCase(chars[0])

    return String(chars)
}

fun String.isInt(): Boolean {
    return try {
        this.toInt()
        true
    } catch (ignored: NumberFormatException) {
        false
    }
}

fun String.isLong(): Boolean {
    return try {
        this.toLong()
        true
    } catch (ignored: NumberFormatException) {
        false
    }
}