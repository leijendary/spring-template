package com.leijendary.spring.template.repository

import com.leijendary.spring.template.core.exception.ResourceNotFoundException
import com.leijendary.spring.template.core.repository.SeekPaginationRepository
import com.leijendary.spring.template.core.repository.SoftDeleteRepository
import com.leijendary.spring.template.entity.SampleTable
import jakarta.persistence.LockModeType.WRITE
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Lock
import java.util.*
import java.util.stream.Stream

private val source = listOf("data", "SampleTable", "id")

interface SampleTableRepository : JpaRepository<SampleTable, UUID>, JpaSpecificationExecutor<SampleTable>,
    SeekPaginationRepository<SampleTable>, SoftDeleteRepository<SampleTable> {
    @Lock(WRITE)
    fun findLockedById(id: UUID): SampleTable?

    fun streamAllByDeletedAtIsNull(): Stream<SampleTable>

    fun findLockedByIdOrThrow(id: UUID) = findLockedById(id) ?: throw ResourceNotFoundException(source, id)
}
