package com.leijendary.domain.ai

import com.leijendary.model.ErrorSource
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table
data class AiChat(var title: String = DEFAULT_TITLE) {
    @Id
    var id: Long = 0

    @CreatedDate
    lateinit var createdAt: Instant

    @CreatedBy
    lateinit var createdBy: String

    @get:Transient
    val isNew: Boolean
        get() = title == DEFAULT_TITLE

    companion object {
        const val DEFAULT_TITLE = "New Chat"
        const val ENTITY = "chat"
        val ERROR_SOURCE = ErrorSource(pointer = "/data/$ENTITY/id")
    }
}
