package com.leijendary.domain.sample

import com.leijendary.domain.sample.Sample.Companion.ENTITY
import com.leijendary.domain.sample.Sample.Companion.ERROR_SOURCE
import com.leijendary.error.exception.ResourceNotFoundException
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
interface SampleRepository : CrudRepository<Sample, String>, PagingAndSortingRepository<Sample, String> {
    fun <T> findByNameContainingIgnoreCase(name: String, pageable: Pageable, type: Class<T>): Page<T>

    fun <T> findBy(pageable: Pageable, type: Class<T>): Page<T>

    fun <T> findById(id: String, type: Class<T>): Optional<T>

    @Query(
        """
        select id, name, description, amount, created_at
        from sample
        where
            name ilike concat('%%', :query::text, '%%')
            and (
                :#{#cursorable.timestamp}::timestamp is null
                or :#{#cursorable.id}::text is null
                or (created_at, id) < (:#{#cursorable.timestamp}, :#{#cursorable.id})
            )
        order by created_at desc
        limit :#{#cursorable.limit}
        """
    )
    fun <T> cursor(query: String?, cursorable: Cursorable, type: Class<T>): MutableList<T>

    fun <T> streamBy(type: Class<T>): Stream<T>
}

@Transactional(readOnly = true)
fun <T> SampleRepository.findByIdOrThrow(id: String, type: Class<T>): T {
    return findById(id, type).orElseThrow { ResourceNotFoundException(id, ENTITY, ERROR_SOURCE) }
}

@Transactional(readOnly = true)
fun SampleRepository.findByIdOrThrow(id: String): Sample {
    return findById(id).orElseThrow { ResourceNotFoundException(id, ENTITY, ERROR_SOURCE) }
}
