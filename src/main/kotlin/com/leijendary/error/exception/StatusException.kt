package com.leijendary.error.exception

import org.springframework.http.HttpStatus

class StatusException(
    val code: String,
    val status: HttpStatus,
    val pointer: String,
    val arguments: Array<Any> = emptyArray()
) : RuntimeException("$status: $code $pointer $arguments")
