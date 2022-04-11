package com.leijendary.spring.boot.template.repository

import com.leijendary.spring.boot.template.core.repository.SeekPaginationRepository
import com.leijendary.spring.boot.template.core.repository.SoftDeleteRepository
import com.leijendary.spring.boot.template.model.SampleTable
import org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE
import org.springframework.data.jpa.repository.*
import java.util.*
import java.util.stream.Stream
import javax.persistence.LockModeType.PESSIMISTIC_FORCE_INCREMENT
import javax.persistence.QueryHint

interface SampleTableRepository : JpaRepository<SampleTable, UUID>, JpaSpecificationExecutor<SampleTable>,
    SeekPaginationRepository<SampleTable>, SoftDeleteRepository<SampleTable> {

    @Lock(PESSIMISTIC_FORCE_INCREMENT)
    fun findLockedById(id: UUID): SampleTable?

    @QueryHints(value = [QueryHint(name = HINT_FETCH_SIZE, value = "1")])
    @Query("select s from SampleTable s where s.deletedAt is null")
    fun streamAll(): Stream<SampleTable>
}