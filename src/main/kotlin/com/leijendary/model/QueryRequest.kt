package com.leijendary.model

import com.fasterxml.jackson.annotation.JsonIgnore

data class QueryRequest(val query: String? = null) {
    @get:JsonIgnore
    val isEmpty: Boolean
        get() = query.isNullOrBlank()
}
