package com.leijendary.extension

import com.leijendary.util.objectMapper
import org.springframework.core.io.ClassPathResource
import org.springframework.util.StreamUtils
import java.lang.Character.toLowerCase
import java.lang.Character.toUpperCase
import kotlin.text.Charsets.UTF_8

private val REGEX_UNDERSCORE_ALPHA = "_[a-z]".toRegex()
private val REGEX_COMPACT_STRING = "(\\n\\s*|\\s{2,})".toRegex()

inline fun <reified T> String.toClass(): T {
    return objectMapper.readValue(this, T::class.java)
}

fun String.snakeCaseToCamelCase(capitalizeFirst: Boolean = false): String {
    val result = replace(REGEX_UNDERSCORE_ALPHA) {
        it.value.last().uppercase()
    }

    return if (capitalizeFirst) result.upperCaseFirst() else result
}

fun String.camelCaseToSnakeCase(): String {
    val builder = fold(StringBuilder()) { acc, c ->
        val lower = c.lowercase()
        val value = if (acc.isNotEmpty() && c.isUpperCase()) "_$lower" else lower

        acc.append(value)
    }

    return builder.toString()
}

fun String.upperCaseFirst(): String {
    val chars = toCharArray()
    chars[0] = toUpperCase(chars[0])

    return String(chars)
}

fun String.lowerCaseFirst(): String {
    val chars = toCharArray()
    chars[0] = toLowerCase(chars[0])

    return String(chars)
}

fun String.isInt(): Boolean {
    return toIntOrNull() !== null
}

fun String.isLong(): Boolean {
    return toLongOrNull() !== null
}

fun String.content(compact: Boolean = true): String {
    var string = ClassPathResource(this).inputStream.use { StreamUtils.copyToString(it, UTF_8) }

    if (compact) {
        string = string.replace(REGEX_COMPACT_STRING, " ")
    }

    return string
}
