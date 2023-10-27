package com.leijendary.spring.core.projection

import org.springframework.jdbc.core.RowMapper

interface JdbcQueryProjection<T> : RowMapper<T> {
    val sql: String
}
