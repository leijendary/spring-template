package com.leijendary.spring.template.core.repository

import com.leijendary.spring.template.core.data.Seek
import com.leijendary.spring.template.core.data.Seekable
import com.leijendary.spring.template.core.model.UUIDModel
import org.springframework.data.jpa.domain.Specification
import kotlin.reflect.KClass

interface SeekPaginationRepository<T : UUIDModel> {
    fun findAll(entity: KClass<T>, seekable: Seekable = Seekable()): Seek<T>

    fun findAll(entity: KClass<T>, specification: Specification<T>?, seekable: Seekable = Seekable()): Seek<T>
}