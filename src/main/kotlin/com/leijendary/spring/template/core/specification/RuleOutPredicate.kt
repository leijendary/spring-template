package com.leijendary.spring.template.core.specification

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Predicate

object RuleOutPredicate {
    fun withFilter(path: Path<String>, filters: List<String>, builder: CriteriaBuilder): Predicate? {
        val includes = filters
            .stream()
            .filter { s -> !s.startsWith("!") }
            .map { obj -> obj.lowercase() }
            .toList()
        val excludes = filters
            .stream()
            .filter { s -> s.startsWith("!") }
            .map { s -> s.replaceFirst("!".toRegex(), "").lowercase() }
            .toList()
        val predicates = ArrayList<Predicate>()
        val expression = builder.lower(path)

        if (includes.isNotEmpty()) {
            val `in` = expression.`in`(includes)

            predicates.add(`in`)
        }

        if (excludes.isNotEmpty()) {
            val notIn = builder.not(expression.`in`(excludes))

            predicates.add(notIn)
        }

        return if (predicates.isEmpty()) {
            null
        } else {
            builder.and(*predicates.toTypedArray())
        }
    }
}
