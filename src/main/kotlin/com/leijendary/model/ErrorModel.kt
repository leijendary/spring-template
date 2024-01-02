package com.leijendary.model

@JvmRecord
data class ErrorModel(val id: Any? = null, val code: String, val message: String? = null, val source: ErrorSource)

@JvmRecord
data class ErrorSource(
    val pointer: String? = null,
    val parameter: String? = null,
    val header: String? = null,
    val meta: Map<String, Any>? = null
)
