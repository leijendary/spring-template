package com.leijendary.spring.microservicetemplate.specification;

import com.leijendary.spring.microservicetemplate.model.LocaleModel;
import com.leijendary.spring.microservicetemplate.model.LocalizedModel;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static com.leijendary.spring.microservicetemplate.util.RequestContextUtil.getLanguage;
import static javax.persistence.criteria.JoinType.LEFT;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Builder
public class LocaleSpecification<R extends LocalizedModel<R, T>, T extends LocaleModel<R>> implements Specification<R> {

    private final String language = getLanguage();

    @Override
    public Predicate toPredicate(@NonNull final Root<R> root, @NonNull final CriteriaQuery<?> criteriaQuery,
                                 @NonNull final CriteriaBuilder criteriaBuilder) {
        // If there is no language filter, return all
        if (isBlank(language)) {
            return criteriaQuery.where().getRestriction();
        }

        final var translationsSubQuery = criteriaQuery.subquery(LocaleModel.class);
        final var translationsSubQueryRoot = translationsSubQuery.from(LocaleModel.class);

        final var translationsJoin = root.joinSet("translations", LEFT);
        final var languagePath = translationsJoin.get("language");
        final var languagePredicate = criteriaBuilder.equal(languagePath, language);

        translationsSubQuery
                .select(translationsSubQueryRoot)
                .where(languagePredicate);

        return criteriaQuery.where().getRestriction();
    }
}
