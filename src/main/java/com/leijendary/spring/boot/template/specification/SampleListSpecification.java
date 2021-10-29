package com.leijendary.spring.boot.template.specification;

import com.leijendary.spring.boot.template.model.SampleTable;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static com.leijendary.spring.boot.core.specification.TranslationPredicate.query;
import static com.leijendary.spring.boot.core.util.PredicateUtil.lowerLike;
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

        // Column 1 filtering
        final var column1Path = root.<String>get("column1");
        final var column1Like = lowerLike(query, column1Path, criteriaBuilder);
        final var translations = query(root, criteriaBuilder, query, "name", "description");

        final var predicate = criteriaBuilder.or(column1Like, translations);

        return criteriaQuery.where(predicate).getRestriction();
    }
}
