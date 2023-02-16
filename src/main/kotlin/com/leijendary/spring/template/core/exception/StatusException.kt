package com.leijendary.spring.template.core.exception

import org.springframework.http.HttpStatus

class StatusException(
    val source: List<String>,
    val code: String,
    val status: HttpStatus,
    val arguments: Array<Any> = emptyArray()
) : RuntimeException()
