package com.leijendary.model

data class ErrorModel(val id: Any? = null, val code: String, val message: String? = null, val source: ErrorSource)

data class ErrorSource(
    val pointer: String? = null,
    val parameter: String? = null,
    val header: String? = null,
    val meta: MutableMap<String, Any>? = null
)
