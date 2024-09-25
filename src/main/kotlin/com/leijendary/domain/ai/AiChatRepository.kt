package com.leijendary.domain.ai

import com.leijendary.model.Cursorable
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Transactional(readOnly = true)
interface AiChatRepository : CrudRepository<AiChat, Long>, PagingAndSortingRepository<AiChat, Long> {
    fun <T> findFirstByIdAndCreatedBy(id: Long, createdBy: String, type: Class<T>): Optional<T>

    @Query(
        """
        select id, title, created_at
        from ai_chat
        where
            created_by = :createdBy
            and (
                :#{#cursorable.timestamp}::timestamp is null
                or :#{#cursorable.id}::bigint is null
                or (created_at, id) < (:#{#cursorable.timestamp}, :#{#cursorable.id})
            )
        order by created_at desc, id desc
        limit :#{#cursorable.limit}
        """
    )
    fun <T> cursor(createdBy: String, cursorable: Cursorable, type: Class<T>): MutableList<T>
}
