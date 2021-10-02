package com.leijendary.spring.microservicetemplate.specification;

import com.leijendary.spring.microservicetemplate.model.LocalizedModel;
import lombok.Builder;
import org.hibernate.query.criteria.internal.OrderImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import javax.persistence.criteria.*;
import java.util.ArrayList;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Builder
public class LocalizedSpecification<T extends LocalizedModel<?>> implements Specification<T> {

    private final String language;

    @Override
    public Predicate toPredicate(@NonNull final Root<T> root, @NonNull final CriteriaQuery<?> criteriaQuery,
                                 @NonNull final CriteriaBuilder criteriaBuilder) {
        final var predicates = new ArrayList<Predicate>();

        // If there is no language filter, return all based on the reference ID predicate
        if (isBlank(language)) {
            return criteriaQuery.where().getRestriction();
        }

        // ID path for reference ID
        final var idPath = root.<Long>get("id");
        // Join translations for filtering
        final var translationJoin = root.join("translations");

        final var languagePath = translationJoin.<String>get("language");
        // Criteria for exact language
        final var languageEquals = criteriaBuilder.equal(languagePath, language);
        // Parent query ordinal
        final var ordinalPath = translationJoin.<Integer>get("ordinal");

        // Sub query start
        final var type = translationJoin.getJavaType();
        final var ordinalSubQuery = criteriaQuery.subquery(Integer.class);
        final var subQueryRoot = ordinalSubQuery.from(type);
        final var subQueryOrdinalPath = subQueryRoot.<Integer>get("ordinal");
        // Criteria for the lowest ordinal (the default)
        final var subQueryOrdinalMin = criteriaBuilder.min(subQueryOrdinalPath);
        // Filter sub query reference id
        final var subQueryReferenceId = referenceId(subQueryRoot, idPath, criteriaBuilder);
        // Sub query filtering
        ordinalSubQuery.select(subQueryOrdinalMin).where(subQueryReferenceId);

        // Compare the ordinal of the main query vs the sub query
        final var ordinalEqual = criteriaBuilder.equal(ordinalPath, ordinalSubQuery);

        // language OR first ordinal
        final var languageOrFirstOrdinal = criteriaBuilder.or(languageEquals, ordinalEqual);

        predicates.add(languageOrFirstOrdinal);

        // Descending order of ordinal to get the actual language
        // filter first before the default
        final var ordinalDesc = new OrderImpl(ordinalPath).reverse();

        return criteriaQuery
                .where(predicates.toArray(new Predicate[0]))
                .orderBy(ordinalDesc)
                .getRestriction();
    }

    private Predicate referenceId(final Root<?> root, final Path<Long> referenceId,
                                  final CriteriaBuilder criteriaBuilder) {
        // Filter first using the referenceId
        final var referencePath = root.get("reference");
        final var referenceIdPath = referencePath.<Long>get("id");

        return criteriaBuilder.equal(referenceIdPath, referenceId);
    }
}
