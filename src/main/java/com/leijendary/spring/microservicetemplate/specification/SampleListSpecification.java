package com.leijendary.spring.microservicetemplate.specification;

import com.leijendary.spring.microservicetemplate.model.SampleTable;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

import static org.apache.commons.lang.StringUtils.isBlank;

@Builder
public class SampleListSpecification implements Specification<SampleTable> {

    private final String column1;

    @Override
    public Predicate toPredicate(Root<SampleTable> root, CriteriaQuery<?> criteriaQuery,
                                 CriteriaBuilder criteriaBuilder) {
        if (isBlank(this.column1)) {
            return criteriaQuery.where().getRestriction();
        }

        Path<String> column1 = root.get("column1");
        Expression<String> upperColumn1 = criteriaBuilder.upper(column1);
        Predicate like = criteriaBuilder.like(upperColumn1, "%" + this.column1.toUpperCase() + "%");

        return criteriaQuery.where(like).getRestriction();
    }
}
