package com.leijendary.spring.boot.template.util;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

public class PredicateUtil {

    public static Predicate lowerLike(final String query, final Path<String> path,
                                      final CriteriaBuilder criteriaBuilder) {
        final var lowerQuery = query.toLowerCase();
        final var lowerPath = criteriaBuilder.lower(path);

        return criteriaBuilder.like(lowerPath, "%" + lowerQuery + "%");
    }

    public static Predicate lowerEqual(final String value, final Path<String> path,
                                       final CriteriaBuilder criteriaBuilder) {
        final var lowerValue = value.toLowerCase();
        final var lowerPath = criteriaBuilder.lower(path);

        return criteriaBuilder.equal(lowerPath, lowerValue);
    }
}
