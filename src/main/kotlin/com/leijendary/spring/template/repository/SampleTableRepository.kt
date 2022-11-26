package com.leijendary.spring.template.repository

import com.leijendary.spring.template.core.repository.SeekPaginationRepository
import com.leijendary.spring.template.core.repository.SoftDeleteRepository
import com.leijendary.spring.template.model.SampleTable
import jakarta.persistence.LockModeType.WRITE
import jakarta.persistence.QueryHint
import org.hibernate.jpa.AvailableHints.HINT_FETCH_SIZE
import org.springframework.data.jpa.repository.*
import java.util.*
import java.util.stream.Stream

interface SampleTableRepository : JpaRepository<SampleTable, UUID>, JpaSpecificationExecutor<SampleTable>,
    SeekPaginationRepository<SampleTable>, SoftDeleteRepository<SampleTable> {
    @Lock(WRITE)
    fun findLockedById(id: UUID): SampleTable?

    @QueryHints(value = [QueryHint(name = HINT_FETCH_SIZE, value = "1")])
    @Query("select s from SampleTable s where s.deletedAt is null")
    fun streamAll(): Stream<SampleTable>
}