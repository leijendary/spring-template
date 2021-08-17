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

        // Column 1 filtering
        final var column1Path = root.<String>get("column1");
        final var column1Like = lowerLike(query, column1Path, criteriaBuilder);

        // Translations join and paths
        final var translationJoin = root.join("translations");
        final var namePath = translationJoin.<String>get("name");
        final var descriptionPath = translationJoin.<String>get("description");

        // Name or description translation like query
        final var nameLike = criteriaBuilder.like(namePath, "%" + query + "%");
        final var descriptionLike = criteriaBuilder.like(descriptionPath, "%" + query + "%");

        final var predicate = criteriaBuilder.or(column1Like, nameLike, descriptionLike);

        return criteriaQuery.where(predicate).getRestriction();
    }
}
