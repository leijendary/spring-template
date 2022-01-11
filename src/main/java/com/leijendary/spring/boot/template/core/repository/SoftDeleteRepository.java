package com.leijendary.spring.boot.template.core.repository;

import com.leijendary.spring.boot.template.core.model.SoftDeleteModel;

public interface SoftDeleteRepository<T extends SoftDeleteModel> {

    void softDelete(final T entity);
}
