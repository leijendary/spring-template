package com.leijendary.spring.microservicetemplate.specification;

import com.leijendary.spring.microservicetemplate.model.SampleTable;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Builder
public class SampleListSpecification implements Specification<SampleTable> {

    private final String column1;

    @Override
    public Predicate toPredicate(final Root<SampleTable> root, final CriteriaQuery<?> criteriaQuery,
                                 final CriteriaBuilder criteriaBuilder) {
        if (isBlank(this.column1)) {
            return criteriaQuery.where().getRestriction();
        }

        final var column1 = root.<String>get("column1");
        final var lowerColumn1 = criteriaBuilder.lower(column1);
        final var like = criteriaBuilder.like(lowerColumn1, "%" + this.column1.toLowerCase() + "%");

        return criteriaQuery.where(like).getRestriction();
    }
}
