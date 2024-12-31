package com.leijendary.domain.sample

import com.leijendary.domain.sample.Sample.Companion.ENTITY
import com.leijendary.domain.sample.Sample.Companion.ERROR_SOURCE
import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.model.Cursorable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.stream.Stream

interface SampleCursorRepository {
    fun <T> cursor(query: String?, cursorable: Cursorable, type: Class<T>): MutableList<T>
}

@Transactional(readOnly = true)
interface SampleRepository : CrudRepository<Sample, String>, PagingAndSortingRepository<Sample, String>,
    SampleCursorRepository {
    fun <T> findByNameContainingIgnoreCase(name: String, pageable: Pageable, type: Class<T>): Page<T>

    fun <T> findBy(pageable: Pageable, type: Class<T>): Page<T>

    fun <T> findById(id: String, type: Class<T>): Optional<T>

    fun <T> streamBy(type: Class<T>): Stream<T>
}

@Repository
class SampleCursorRepositoryImpl(private val jdbcClient: JdbcClient) : SampleCursorRepository {
    override fun <T> cursor(query: String?, cursorable: Cursorable, type: Class<T>): MutableList<T> {
        return jdbcClient.sql(SQL)
            .param("query", query)
            .param("timestamp", cursorable.timestamp)
            .param("id", cursorable.id)
            .param("limit", cursorable.limit)
            .query(type)
            .list()
    }

    companion object {
        private const val SQL = """
            SELECT id, name, description, amount, created_at
            FROM sample
            WHERE
                name ILIKE CONCAT('%%', :query::text, '%%')
                AND (
                    :timestamp::timestamp IS NULL
                    OR :id::text IS NULL
                    OR (created_at, id) < (:timestamp, :id)
                )
            ORDER BY created_at DESC
            LIMIT :limit
            """
    }
}

@Transactional(readOnly = true)
fun <T> SampleRepository.findByIdOrThrow(id: String, type: Class<T>): T {
    return findById(id, type).orElseThrow { ResourceNotFoundException(id, ENTITY, ERROR_SOURCE) }
}

@Transactional(readOnly = true)
fun SampleRepository.findByIdOrThrow(id: String): Sample {
    return findById(id).orElseThrow { ResourceNotFoundException(id, ENTITY, ERROR_SOURCE) }
}
