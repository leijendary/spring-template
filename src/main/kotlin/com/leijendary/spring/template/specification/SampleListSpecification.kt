package com.leijendary.spring.template.specification

import com.leijendary.spring.template.core.extension.lowerLike
import com.leijendary.spring.template.entity.SampleTable
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification

class SampleListSpecification(private val query: String? = null) : Specification<SampleTable> {
    override fun toPredicate(
        root: Root<SampleTable>,
        criteriaQuery: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate {
        if (query.isNullOrBlank()) {
            return criteriaBuilder.and()
        }

        // Column 1 filtering
        val column1Path = root.get<String>("column1")
        val column1Like = lowerLike(query, column1Path, criteriaBuilder)

        return criteriaQuery.where(column1Like).restriction
    }
}
