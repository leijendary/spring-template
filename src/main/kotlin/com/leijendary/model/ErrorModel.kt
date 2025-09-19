package com.leijendary.model

data class ErrorModel(val code: String, val message: String? = null, val pointer: String, val id: Any? = null)
