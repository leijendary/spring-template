package com.leijendary.model

import org.springframework.aot.hint.annotation.RegisterReflection

@RegisterReflection
data class QueryRequest(val query: String? = null)
