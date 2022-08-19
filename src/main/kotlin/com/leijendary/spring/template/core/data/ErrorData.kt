package com.leijendary.spring.template.core.data

data class ErrorData(val source: List<Any>, val code: String, val message: String? = null)