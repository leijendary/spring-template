package com.leijendary.projection

import org.springframework.data.domain.Persistable

interface PrefixedIDProjection : Persistable<String> {
    fun getPrefix(): String

    fun setId(id: String)
}
