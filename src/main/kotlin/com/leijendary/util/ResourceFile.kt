package com.leijendary.util

import org.springframework.core.io.ClassPathResource
import org.springframework.util.StreamUtils
import kotlin.text.Charsets.UTF_8

fun includeString(path: String): String {
    val resource = ClassPathResource(path)

    return resource.inputStream.use {
        StreamUtils.copyToString(it, UTF_8)
    }
}
