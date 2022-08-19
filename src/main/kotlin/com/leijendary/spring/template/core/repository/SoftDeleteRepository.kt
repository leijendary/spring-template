package com.leijendary.spring.template.core.repository

import com.leijendary.spring.template.core.model.SoftDeleteModel

interface SoftDeleteRepository<T : SoftDeleteModel> {
    fun softDelete(entity: T)
}