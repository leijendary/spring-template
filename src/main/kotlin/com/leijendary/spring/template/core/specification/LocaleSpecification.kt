package com.leijendary.spring.template.core.specification

import com.leijendary.spring.template.core.model.LocaleModel
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification

class LocaleSpecification<T : LocaleModel>(
    private val referenceId: Long = 0,
    private val language: String? = null
) : Specification<T> {
    override fun toPredicate(
        root: Root<T>,
        criteriaQuery: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val referenceId = referenceId(root, criteriaBuilder)
        val predicates = mutableListOf(referenceId)

        // If there is no language filter, return all based on the reference ID predicate
        if (language.isNullOrBlank()) {
            return criteriaQuery.where(referenceId).restriction
        }

        val languagePath = root.get<String>("language")
        // Criteria for exact language
        val languageEquals = criteriaBuilder.equal(languagePath, language)
        // Parent query ordinal
        val ordinalPath = root.get<Int>("ordinal")

        // Sub query start
        val type = root.javaType
        val ordinalSubQuery = criteriaQuery.subquery(Int::class.java)
        val subQueryRoot = ordinalSubQuery.from(type)
        val subQueryOrdinalPath = subQueryRoot.get<Int>("ordinal")
        // Criteria for the lowest ordinal (the default)
        val subQueryOrdinalMin = criteriaBuilder.min(subQueryOrdinalPath)
        // Filter sub query reference id
        val subQueryReferenceId = referenceId(subQueryRoot, criteriaBuilder)

        // Sub query filtering
        ordinalSubQuery.select(subQueryOrdinalMin).where(subQueryReferenceId)

        // Compare the ordinal of the main query vs the sub query
        val ordinalEqual = criteriaBuilder.equal(ordinalPath, ordinalSubQuery)

        // language OR first ordinal
        val languageOrFirstOrdinal = criteriaBuilder.or(languageEquals, ordinalEqual)
        predicates.add(languageOrFirstOrdinal)

        // Descending order of ordinal to get the actual language
        // filter first before the default
        val ordinalDesc = criteriaBuilder.desc(ordinalPath)

        return criteriaQuery
            .where(*predicates.toTypedArray())
            .orderBy(ordinalDesc)
            .restriction
    }

    private fun referenceId(root: Root<*>, criteriaBuilder: CriteriaBuilder): Predicate {
        // Filter first using the referenceId
        val referencePath = root.get<Any>("reference")
        val referenceIdPath = referencePath.get<Long>("id")

        return criteriaBuilder.equal(referenceIdPath, referenceId)
    }
}
