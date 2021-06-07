package com.leijendary.spring.microservicetemplate.specification;

import com.leijendary.spring.microservicetemplate.model.SampleTable;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

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
        final var upperColumn1 = criteriaBuilder.upper(column1);
        final var like = criteriaBuilder.like(upperColumn1, "%" + this.column1.toUpperCase() + "%");

        return criteriaQuery.where(like).getRestriction();
    }
}
