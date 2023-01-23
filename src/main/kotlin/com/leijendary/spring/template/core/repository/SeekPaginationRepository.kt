package com.leijendary.spring.template.core.repository

import com.leijendary.spring.template.core.model.Seek
import com.leijendary.spring.template.core.model.Seekable
import com.leijendary.spring.template.core.entity.UUIDEntity
import org.springframework.data.jpa.domain.Specification
import kotlin.reflect.KClass

interface SeekPaginationRepository<T : UUIDEntity> {
    fun findAll(entity: KClass<T>, seekable: Seekable = Seekable()): Seek<T>

    fun findAll(entity: KClass<T>, specification: Specification<T>?, seekable: Seekable = Seekable()): Seek<T>
}
