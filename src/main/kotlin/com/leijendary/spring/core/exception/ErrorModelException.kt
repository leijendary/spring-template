package com.leijendary.spring.core.exception

import com.leijendary.spring.core.model.ErrorModel
import org.springframework.http.HttpStatus

class ErrorModelException(val status: HttpStatus, val errors: List<ErrorModel>) : RuntimeException()
