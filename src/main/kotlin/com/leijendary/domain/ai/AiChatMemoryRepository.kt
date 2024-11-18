package com.leijendary.domain.ai

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
interface AiChatMemoryRepository : CrudRepository<AiChatMemory, String> {
    fun findBySessionId(sessionId: String, pageable: Pageable): Page<AiChatMemory>
}
