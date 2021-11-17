package com.leijendary.spring.boot.template.specification;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.ArrayList;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.leijendary.spring.boot.template.model.LocaleModel;

import org.hibernate.query.criteria.internal.OrderImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import lombok.Builder;

@Builder
public class LocaleSpecification<T extends LocaleModel> implements Specification<T> {

    private final long referenceId;
    private final String language;

    @Override
    public Predicate toPredicate(@NonNull final Root<T> root, @NonNull final CriteriaQuery<?> criteriaQuery,
                                 @NonNull final CriteriaBuilder criteriaBuilder) {
        final var referenceId = referenceId(root, criteriaBuilder);
        final var predicates = new ArrayList<>(singletonList(referenceId));

        // If there is no language filter, return all based on the reference ID predicate
        if (isBlank(language)) {
            return criteriaQuery.where(referenceId).getRestriction();
        }

        final var languagePath = root.<String>get("language");
        // Criteria for exact language
        final var languageEquals = criteriaBuilder.equal(languagePath, language);
        // Parent query ordinal
        final var ordinalPath = root.<Integer>get("ordinal");

        // Sub query start
        final var type = root.getJavaType();
        final var ordinalSubQuery = criteriaQuery.subquery(Integer.class);
        final var subQueryRoot = ordinalSubQuery.from(type);
        final var subQueryOrdinalPath = subQueryRoot.<Integer>get("ordinal");
        // Criteria for the lowest ordinal (the default)
        final var subQueryOrdinalMin = criteriaBuilder.min(subQueryOrdinalPath);
        // Filter sub query reference id
        final var subQueryReferenceId = referenceId(subQueryRoot, criteriaBuilder);
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

    private Predicate referenceId(final Root<?> root, final CriteriaBuilder criteriaBuilder) {
        // Filter first using the referenceId
        final var referencePath = root.get("reference");
        final var referenceIdPath = referencePath.<Long>get("id");

        return criteriaBuilder.equal(referenceIdPath, referenceId);
    }
}
