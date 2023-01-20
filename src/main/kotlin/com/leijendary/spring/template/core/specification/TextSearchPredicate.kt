package com.leijendary.spring.template.core.specification

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Predicate

object TextSearchPredicate {
    fun withQuery(path: Path<*>, query: String, builder: CriteriaBuilder): Predicate? {
        val filter = query.split(" ").joinToString(" | ")
        val tsQuery = builder.function("to_tsquery", String::class.java, builder.literal(filter))
        val tsRank = builder
            .function("ts_rank", Double::class.java, path, tsQuery)
            .`as`(Double::class.java)

        return builder.greaterThan(tsRank, 0.0)
    }
}
