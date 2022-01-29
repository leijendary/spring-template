package com.leijendary.spring.boot.template.core.repository

import com.leijendary.spring.boot.template.core.model.SoftDeleteModel

interface SoftDeleteRepository<T : SoftDeleteModel?> {
    fun softDelete(entity: T)
}