package com.leijendary.spring.boot.template.repository;

import com.leijendary.spring.boot.template.model.SoftDeleteModel;

public interface SoftDeleteRepository<T extends SoftDeleteModel> {

    void softDelete(final T entity);
}
