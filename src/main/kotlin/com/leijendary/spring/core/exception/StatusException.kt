package com.leijendary.spring.core.exception

import com.leijendary.spring.core.model.ErrorSource
import org.springframework.http.HttpStatus

class StatusException(
    val code: String,
    val status: HttpStatus,
    val source: ErrorSource,
    val arguments: Array<Any> = emptyArray()
) : RuntimeException()
