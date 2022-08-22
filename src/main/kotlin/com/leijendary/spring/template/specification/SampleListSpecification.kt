package com.leijendary.spring.template.specification

import com.leijendary.spring.template.core.util.Predicate.lowerLike
import com.leijendary.spring.template.model.SampleTable
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
        if (query.isNullOrBlank()) {
            return criteriaQuery.where().restriction
        }

        // Column 1 filtering
        val column1Path = root.get<String>("column1")
        val column1Like = lowerLike(query, column1Path, criteriaBuilder)

        return criteriaQuery.where(column1Like).restriction
    }
}