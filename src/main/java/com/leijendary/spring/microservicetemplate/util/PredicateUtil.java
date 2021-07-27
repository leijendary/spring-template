package com.leijendary.spring.microservicetemplate.util;

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
}
