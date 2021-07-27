package com.leijendary.spring.microservicetemplate.specification;

import com.leijendary.spring.microservicetemplate.model.SampleTable;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static com.leijendary.spring.microservicetemplate.util.PredicateUtil.lowerLike;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Builder
public class SampleListSpecification implements Specification<SampleTable> {

    private final String query;

    @Override
    public Predicate toPredicate(@NonNull final Root<SampleTable> root, @NonNull final CriteriaQuery<?> criteriaQuery,
                                 @NonNull final CriteriaBuilder criteriaBuilder) {
        if (isBlank(query)) {
            return criteriaQuery.where().getRestriction();
        }

        final var column1 = root.<String>get("column1");
        final var column1Like = lowerLike(query, column1, criteriaBuilder);

        return criteriaQuery.where(column1Like).getRestriction();
    }
}
