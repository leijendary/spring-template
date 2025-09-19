package com.leijendary.domain.ai.chat

import com.leijendary.domain.ai.chat.AiChat.Companion.ENTITY
import com.leijendary.domain.ai.chat.AiChat.Companion.POINTER_ID
import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.model.Cursorable
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*

private const val QUERY_CURSOR = """
SELECT id, title, created_at
FROM ai_chat
WHERE
    created_by = :createdBy
    AND (
        :#{#cursorable.timestamp}::timestamp IS NULL
        OR :#{#cursorable.id}::text IS NULL
        OR (created_at, id) < (:#{#cursorable.timestamp}, :#{#cursorable.id})
    )
ORDER BY created_at DESC
LIMIT :#{#cursorable.limit}
"""

@Transactional(readOnly = true)
interface AiChatRepository : CrudRepository<AiChat, String>, PagingAndSortingRepository<AiChat, String> {
    fun existsByIdAndCreatedBy(id: String, createdBy: String): Boolean

    fun <T> findFirstByIdAndCreatedBy(id: String, createdBy: String, type: Class<T>): Optional<T>

    @Query(QUERY_CURSOR)
    fun cursor(createdBy: String, cursorable: Cursorable): List<AiChatCursor>
}

@Transactional(readOnly = true)
fun <T> AiChatRepository.findFirstByIdAndCreatedByOrThrow(id: String, createdBy: String, type: Class<T>): T {
    return findFirstByIdAndCreatedBy(id, createdBy, type)
        .orElseThrow { ResourceNotFoundException(id, ENTITY, POINTER_ID) }
}
