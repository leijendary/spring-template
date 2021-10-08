package com.leijendary.spring.microservicetemplate.specification;

import com.leijendary.spring.microservicetemplate.model.LocaleModel;
import com.leijendary.spring.microservicetemplate.model.LocalizedModel;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;

import static com.leijendary.spring.microservicetemplate.util.PredicateUtil.lowerLike;
import static java.util.Arrays.stream;
import static javax.persistence.criteria.JoinType.LEFT;

public class TranslationPredicate {

    public static <R extends LocalizedModel<T>, T extends LocaleModel> Predicate query(
            final Root<R> root, final CriteriaBuilder criteriaBuilder, final String query, final String... paths) {
        // Array of predicates as the paths are dynamic
        final var predicates = new ArrayList<Predicate>();
        // Get the translation path to be used later
        final var translationJoin = root.<R, T>joinSet("translations", LEFT);

        stream(paths).forEach(path -> {
            final var stringPath = translationJoin.<String>get(path);
            final var stringLike = lowerLike(query, stringPath, criteriaBuilder);

            predicates.add(stringLike);
        });

        return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
    }
}
