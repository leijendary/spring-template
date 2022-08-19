package com.leijendary.spring.template.core.specification

import com.leijendary.spring.template.core.model.LocalizedModel
import org.apache.commons.lang3.StringUtils.isBlank
import org.hibernate.query.criteria.internal.OrderImpl
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.*

class LocalizedSpecification<T : LocalizedModel<*>>(val language: String? = null) : Specification<T> {
    override fun toPredicate(
        root: Root<T>,
        criteriaQuery: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val predicates = ArrayList<Predicate>()

        // If there is no language filter, return all based on the reference ID predicate
        if (isBlank(language)) {
            return criteriaQuery.where().restriction
        }

        // ID path for reference ID
        val idPath = root.get<Long>("id")
        // Join translations for filtering
        val translationJoin = root.join<Any, Any>("translations")
        val languagePath = translationJoin.get<String>("language")
        // Criteria for exact language
        val languageEquals: Predicate = criteriaBuilder.equal(languagePath, language)
        // Parent query ordinal
        val ordinalPath = translationJoin.get<Int>("ordinal")

        // Sub query start
        val type = translationJoin.javaType
        val ordinalSubQuery = criteriaQuery.subquery(Int::class.java)
        val subQueryRoot = ordinalSubQuery.from(type)
        val subQueryOrdinalPath = subQueryRoot.get<Int>("ordinal")
        // Criteria for the lowest ordinal (the default)
        val subQueryOrdinalMin: Expression<Int> = criteriaBuilder.min(subQueryOrdinalPath)
        // Filter sub query reference id
        val subQueryReferenceId = referenceId(subQueryRoot, idPath, criteriaBuilder)

        // Sub query filtering
        ordinalSubQuery.select(subQueryOrdinalMin).where(subQueryReferenceId)

        // Compare the ordinal of the main query vs the sub query
        val ordinalEqual: Predicate = criteriaBuilder.equal(ordinalPath, ordinalSubQuery)

        // language OR first ordinal
        val languageOrFirstOrdinal: Predicate = criteriaBuilder.or(languageEquals, ordinalEqual)
        predicates.add(languageOrFirstOrdinal)

        // Descending order of ordinal to get the actual language
        // filter first before the default
        val ordinalDesc: Order = OrderImpl(ordinalPath).reverse()

        return criteriaQuery
            .where(*predicates.toTypedArray())
            .orderBy(ordinalDesc)
            .restriction
    }

    private fun referenceId(root: Root<*>, referenceId: Path<Long>, criteriaBuilder: CriteriaBuilder): Predicate {
        // Filter first using the referenceId
        val referencePath = root.get<Any>("reference")
        val referenceIdPath = referencePath.get<Long>("id")

        return criteriaBuilder.equal(referenceIdPath, referenceId)
    }
}