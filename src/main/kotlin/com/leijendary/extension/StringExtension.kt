package com.leijendary.extension

import com.leijendary.util.BeanContainer.objectMapper
import java.lang.Character.toLowerCase
import java.lang.Character.toUpperCase

private val REGEX_UNDERSCORE_ALPHA = "_[a-z]".toRegex()

inline fun <reified T : Any> String.toClass(): T {
    return objectMapper.readValue(this, T::class.java)
}

fun String.snakeCaseToCamelCase(capitalizeFirst: Boolean = false): String {
    val result = replace(REGEX_UNDERSCORE_ALPHA) {
        it.value.last().uppercase()
    }

    return if (capitalizeFirst) result.upperCaseFirst() else result
}

fun String.camelCaseToSnakeCase(): String {
    val builder = StringBuilder()
    val folded = fold(builder) { acc, c ->
        val lower = c.lowercase()
        val value = if (acc.isNotEmpty() && c.isUpperCase()) "_$lower" else lower

        acc.append(value)
    }

    return folded.toString()
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

fun String.isInt() = when (toIntOrNull()) {
    null -> false
    else -> true
}

fun String.isLong() = when (toLongOrNull()) {
    null -> false
    else -> true
}
