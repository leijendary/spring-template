package com.leijendary.spring.boot.template.core.repository;

import static com.leijendary.spring.boot.template.core.util.RequestContext.USERNAME_SYSTEM;
import static com.leijendary.spring.boot.template.core.util.RequestContext.getUsername;
import static com.leijendary.spring.boot.template.core.util.RequestContext.now;
import static java.util.Optional.ofNullable;

import javax.persistence.EntityManager;

import com.leijendary.spring.boot.template.core.model.SoftDeleteModel;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SoftDeleteRepositoryImpl<T extends SoftDeleteModel> implements SoftDeleteRepository<T> {

    private final EntityManager entityManager;

    @Override
    public void softDelete(final T entity) {
        final var username = ofNullable(getUsername())
                .orElse(USERNAME_SYSTEM);

        entity.setDeletedAt(now());
        entity.setDeletedBy(username);

        entityManager.persist(entity);
    }
}
