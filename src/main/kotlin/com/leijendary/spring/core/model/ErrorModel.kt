package com.leijendary.spring.core.model

data class ErrorSource(val pointer: String? = null, val parameter: String? = null, val header: String? = null)

data class ErrorModel(val id: String? = null, val code: String, val message: String? = null, val source: ErrorSource)
