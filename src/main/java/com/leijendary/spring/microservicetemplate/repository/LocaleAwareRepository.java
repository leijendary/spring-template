package com.leijendary.spring.microservicetemplate.repository;

import com.leijendary.spring.microservicetemplate.model.LocaleModel;
import com.leijendary.spring.microservicetemplate.model.LocalizedModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface LocaleAwareRepository<R extends LocalizedModel<R, T>, T extends LocaleModel<R>, ID> {

    Optional<R> findById(final ID id);

    Page<R> findAll(@Nullable Specification<R> specification, Pageable pageable);

    List<R> findAll();

    R save(final R entity);

    void delete(final R entity);

    long count();
}
