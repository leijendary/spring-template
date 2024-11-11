package com.leijendary.domain.ai

import com.leijendary.domain.sample.Sample.Companion.ENTITY
import com.leijendary.domain.sample.Sample.Companion.ERROR_SOURCE
import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.model.Cursorable
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional(readOnly = true)
interface AiChatRepository : CrudRepository<AiChat, String>, PagingAndSortingRepository<AiChat, String> {
    fun <T> findFirstByIdAndCreatedBy(id: String, createdBy: String, type: Class<T>): Optional<T>

    @Query(
        """
        select id, title, created_at
        from ai_chat
        where
            created_by = :createdBy
            and (
                :#{#cursorable.timestamp}::timestamp is null
                or :#{#cursorable.id}::text is null
                or (created_at, id) < (:#{#cursorable.timestamp}, :#{#cursorable.id})
            )
        order by created_at desc
        limit :#{#cursorable.limit}
        """
    )
    fun <T> cursor(createdBy: String, cursorable: Cursorable, type: Class<T>): MutableList<T>
}

@Transactional(readOnly = true)
fun <T> AiChatRepository.findFirstByIdAndCreatedByOrThrow(id: String, createdBy: String, type: Class<T>): T {
    return findFirstByIdAndCreatedBy(id, createdBy, type)
        .orElseThrow { ResourceNotFoundException(id, ENTITY, ERROR_SOURCE) }
}
