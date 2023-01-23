package com.leijendary.spring.template.core.model

data class ErrorModel(val source: List<Any>, val code: String, val message: String? = null) : Response
