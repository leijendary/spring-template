package com.leijendary.spring.template.core.specification

import com.leijendary.spring.template.core.model.LocaleModel
import com.leijendary.spring.template.core.model.LocalizedModel
import com.leijendary.spring.template.core.util.PredicateUtil.lowerLike
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.JoinType.LEFT
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

object TranslationPredicate {
    fun <R : LocalizedModel<T>?, T : LocaleModel> query(
        root: Root<R>, criteriaBuilder: CriteriaBuilder, query: String, vararg paths: String
    ): Predicate {
        // Array of predicates as the paths are dynamic
        val predicates = ArrayList<Predicate>()
        // Get the translation path to be used later
        val translationJoin = root.joinSet<R, T>("translations", LEFT)

        paths.forEach {
            val stringPath = translationJoin.get<String>(it)
            val stringLike: Predicate = lowerLike(query, stringPath, criteriaBuilder)

            predicates.add(stringLike)
        }

        return criteriaBuilder.or(*predicates.toTypedArray())
    }
}