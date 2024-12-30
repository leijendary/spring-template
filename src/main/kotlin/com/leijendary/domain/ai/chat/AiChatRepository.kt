package com.leijendary.domain.ai.chat

import com.leijendary.domain.ai.chat.AiChat.Companion.ENTITY
import com.leijendary.domain.ai.chat.AiChat.Companion.ERROR_SOURCE
import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.model.Cursorable
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface AiChatCursorRepository {
    fun <T> cursor(createdBy: String, cursorable: Cursorable, type: Class<T>): MutableList<T>
}

@Transactional(readOnly = true)
interface AiChatRepository : CrudRepository<AiChat, String>, PagingAndSortingRepository<AiChat, String>,
    AiChatCursorRepository {
    fun existsByIdAndCreatedBy(id: String, createdBy: String): Boolean

    fun <T> findFirstByIdAndCreatedBy(id: String, createdBy: String, type: Class<T>): Optional<T>
}

class AiChatCursorRepositoryImpl(private val jdbcClient: JdbcClient) : AiChatCursorRepository {
    override fun <T> cursor(createdBy: String, cursorable: Cursorable, type: Class<T>): MutableList<T> {
        return jdbcClient.sql(SQL)
            .param("createdBy", createdBy)
            .param("timestamp", cursorable.timestamp)
            .param("id", cursorable.id)
            .param("limit", cursorable.limit)
            .query(type)
            .list()
    }

    companion object {
        private const val SQL = """
            SELECT id, title, created_at
            FROM ai_chat
            WHERE
                created_by = :createdBy
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
fun <T> AiChatRepository.findFirstByIdAndCreatedByOrThrow(id: String, createdBy: String, type: Class<T>): T {
    return findFirstByIdAndCreatedBy(id, createdBy, type)
        .orElseThrow { ResourceNotFoundException(id, ENTITY, ERROR_SOURCE) }
}
