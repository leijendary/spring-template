package com.leijendary.spring.boot.template.core.extension

import java.net.URI

fun URI.fullPath(): String {
    return query?.let { "$path?$it" } ?: path
}