package com.leijendary.spring.boot.template.core.util

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Path
import javax.persistence.criteria.Predicate

object PredicateUtil {
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
}