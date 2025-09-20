package com.leijendary.model

import org.springframework.aot.hint.annotation.RegisterReflection

@RegisterReflection
data class ErrorModel(val code: String, val message: String? = null, val pointer: String, val id: Any? = null)
