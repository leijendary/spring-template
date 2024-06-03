package com.leijendary.model

import com.fasterxml.jackson.annotation.JsonIgnore

data class QueryRequest(val query: String? = null) {
    @JsonIgnore
    fun isEmpty(): Boolean {
        return query.isNullOrBlank()
    }
}
