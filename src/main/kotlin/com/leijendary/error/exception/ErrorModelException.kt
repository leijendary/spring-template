package com.leijendary.error.exception

import com.leijendary.model.ErrorModel
import org.springframework.http.HttpStatus

class ErrorModelException(val status: HttpStatus, val errors: List<ErrorModel>) : RuntimeException()
