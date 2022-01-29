package com.leijendary.spring.boot.template.core.data

data class ErrorData(val source: List<Any>, val code: String, val message: String? = null)