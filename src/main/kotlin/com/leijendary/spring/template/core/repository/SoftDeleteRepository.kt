package com.leijendary.spring.template.core.repository

import com.leijendary.spring.template.core.entity.SoftDeleteEntity

interface SoftDeleteRepository<T : SoftDeleteEntity> {
    fun softDelete(entity: T)
}
