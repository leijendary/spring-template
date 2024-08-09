package com.leijendary.domain.sample

import com.leijendary.model.Cursorable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.stream.Stream

@Transactional(readOnly = true)
interface SampleRepository : CrudRepository<Sample, Long>, PagingAndSortingRepository<Sample, Long> {
    fun <T> findByNameContainingIgnoreCase(name: String, pageable: Pageable, type: Class<T>): Page<T>

    fun <T> findBy(pageable: Pageable, type: Class<T>): Page<T>

    fun <T> findById(id: Long, type: Class<T>): Optional<T>

    @Query(
        """
        select id, name, description, amount, created_at
        from sample
        where
            name ilike concat('%%', :query::text, '%%')
            and (
                :#{#cursorable.timestamp}::timestamp is null
                or :#{#cursorable.id}::bigint is null
                or (created_at, id) < (:#{#cursorable.timestamp}, :#{#cursorable.id})
            )
        order by created_at desc, id desc
        limit :#{#cursorable.limit}
        """
    )
    fun <T> cursor(query: String?, cursorable: Cursorable, language: String, type: Class<T>): MutableList<T>

    fun <T> streamBy(type: Class<T>): Stream<T>
}
