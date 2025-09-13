package com.leijendary.model

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable

abstract class PrefixedIDEntity : Persistable<String> {
    @Id
    private lateinit var id: String

    abstract fun getIdPrefix(): String

    fun setId(id: String) {
        this.id = id
    }

    override fun getId(): String {
        return id
    }

    override fun isNew(): Boolean {
        return !this::id.isInitialized
    }
}
