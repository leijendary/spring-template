package com.leijendary.domain.sample

import com.leijendary.domain.sample.Sample.Companion.ENTITY
import com.leijendary.domain.sample.Sample.Companion.POINTER_ID
import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.model.Cursorable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.ListCrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.stream.Stream

private const val QUERY_CURSOR = """
SELECT id, name, description, amount, created_at
FROM sample
WHERE
    name ILIKE CONCAT('%%', :query::text, '%%')
    AND (
        :#{#cursorable.timestamp}::timestamp IS NULL
        OR :#{#cursorable.id}::text IS NULL
        OR (created_at, id) < (:#{#cursorable.timestamp}, :#{#cursorable.id})
    )
ORDER BY created_at DESC
LIMIT :#{#cursorable.limit}
"""

@Transactional(readOnly = true)
interface SampleRepository : ListCrudRepository<Sample, String>, PagingAndSortingRepository<Sample, String> {
    fun <T> findByNameContainingIgnoreCase(name: String, pageable: Pageable, type: Class<T>): Page<T>

    fun <T> findBy(pageable: Pageable, type: Class<T>): Page<T>

    fun <T> findById(id: String, type: Class<T>): Optional<T>

    fun <T> streamBy(type: Class<T>): Stream<T>

    @Query(QUERY_CURSOR)
    fun cursor(query: String?, cursorable: Cursorable): List<SampleCursor>
}

@Transactional(readOnly = true)
fun <T> SampleRepository.findByIdOrThrow(id: String, type: Class<T>): T {
    return findById(id, type).orElseThrow { ResourceNotFoundException(id, ENTITY, POINTER_ID) }
}

@Transactional(readOnly = true)
fun SampleRepository.findByIdOrThrow(id: String): Sample {
    return findById(id).orElseThrow { ResourceNotFoundException(id, ENTITY, POINTER_ID) }
}
