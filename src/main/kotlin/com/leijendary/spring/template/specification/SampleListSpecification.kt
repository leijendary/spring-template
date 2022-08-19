package com.leijendary.spring.template.specification

import com.leijendary.spring.template.core.specification.TranslationPredicate.query
import com.leijendary.spring.template.core.util.PredicateUtil.lowerLike
import com.leijendary.spring.template.model.SampleTable
import org.apache.commons.lang3.StringUtils.isBlank
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class SampleListSpecification(private val query: String? = null) : Specification<SampleTable> {

    override fun toPredicate(
        root: Root<SampleTable>,
        criteriaQuery: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate {
        if (isBlank(query)) {
            return criteriaQuery.where().restriction
        }

        // Column 1 filtering
        val column1Path = root.get<String>("column1")
        val column1Like: Predicate = lowerLike(query!!, column1Path, criteriaBuilder)
        val translations: Predicate = query(root, criteriaBuilder, query, "name", "description")
        val predicate: Predicate = criteriaBuilder.or(column1Like, translations)

        return criteriaQuery.where(predicate).restriction
    }
}