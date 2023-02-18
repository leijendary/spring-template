package com.leijendary.spring.template.core.extension

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Predicate

fun lowerLike(query: String, path: Path<String>, criteriaBuilder: CriteriaBuilder): Predicate {
    val lowerQuery = query.lowercase()
    val lowerPath = criteriaBuilder.lower(path)

    return criteriaBuilder.like(lowerPath, "%$lowerQuery%")
}

fun lowerEqual(value: String, path: Path<String>, criteriaBuilder: CriteriaBuilder): Predicate {
    val lowerValue = value.lowercase()
    val lowerPath = criteriaBuilder.lower(path)

    return criteriaBuilder.equal(lowerPath, lowerValue)
}

fun ruleOut(path: Path<String>, filters: List<String>, builder: CriteriaBuilder): Predicate? {
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

fun textSearch(path: Path<*>, query: String, builder: CriteriaBuilder): Predicate? {
    val filter = query.split(" ").joinToString(" | ")
    val tsQuery = builder.function("to_tsquery", String::class.java, builder.literal(filter))
    val tsRank = builder
        .function("ts_rank", Double::class.java, path, tsQuery)
        .`as`(Double::class.java)

    return builder.greaterThan(tsRank, 0.0)
}
