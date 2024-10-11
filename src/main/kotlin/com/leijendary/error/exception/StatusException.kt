package com.leijendary.error.exception

import com.leijendary.model.ErrorSource
import org.springframework.http.HttpStatus

class StatusException(
    val code: String,
    val status: HttpStatus,
    val source: ErrorSource,
    val arguments: Array<Any> = emptyArray()
) : RuntimeException("$status: $code $source $arguments")
