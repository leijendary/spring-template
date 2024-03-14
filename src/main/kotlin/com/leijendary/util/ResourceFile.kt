package com.leijendary.util

import org.springframework.core.io.ClassPathResource
import org.springframework.util.StreamUtils
import kotlin.text.Charsets.UTF_8

fun embedded(path: String): String {
    return ClassPathResource(path).inputStream.use { StreamUtils.copyToString(it, UTF_8) }
}
