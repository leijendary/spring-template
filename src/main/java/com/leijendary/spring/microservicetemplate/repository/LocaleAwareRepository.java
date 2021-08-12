package com.leijendary.spring.microservicetemplate.repository;

import com.leijendary.spring.microservicetemplate.model.LocaleModel;
import com.leijendary.spring.microservicetemplate.model.LocalizedModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface LocaleAwareRepository<R extends LocalizedModel<R, T>, T extends LocaleModel<R>,
        ID extends Serializable> {

    Optional<R> findTranslatedById(final ID id, final String language);

    Optional<R> findTranslatedById(final ID id);

    Page<R> findTranslatedAll(final @Nullable Specification<R> specification, final Pageable pageable,
                              final String language);

    Page<R> findTranslatedAll(final Pageable pageable, final String language);

    Page<R> findTranslatedAll(final Pageable pageable);

    List<R> findTranslatedAll(final String language);

    List<R> findTranslatedAll();
}
