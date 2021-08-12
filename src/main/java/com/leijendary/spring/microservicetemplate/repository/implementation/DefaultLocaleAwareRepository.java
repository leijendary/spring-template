package com.leijendary.spring.microservicetemplate.repository.implementation;

import com.leijendary.spring.microservicetemplate.model.LocaleModel;
import com.leijendary.spring.microservicetemplate.model.LocalizedModel;
import com.leijendary.spring.microservicetemplate.repository.LocaleAwareRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class DefaultLocaleAwareRepository<R extends LocalizedModel<R, T>, T extends LocaleModel<R>, ID>
        implements LocaleAwareRepository<R, T, ID> {

    public DefaultLocaleAwareRepository(final Class<R> domainClass, final EntityManager entityManager) {
        System.out.println(domainClass);
        System.out.println(entityManager);
    }

    @Override
    public Optional<R> findById(ID id) {
        return Optional.empty();
    }

    @Override
    public Page<R> findAll(Specification<R> specification, Pageable pageable) {
        return null;
    }

    @Override
    public List<R> findAll() {
        return null;
    }

    @Override
    public R save(R entity) {
        return null;
    }

    @Override
    public void delete(R entity) {

    }

    @Override
    public long count() {
        return 0;
    }
}
