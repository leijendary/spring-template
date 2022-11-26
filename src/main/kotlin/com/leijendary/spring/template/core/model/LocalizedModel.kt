package com.leijendary.spring.template.core.model

import com.leijendary.spring.template.core.util.RequestContext.language
import jakarta.persistence.CollectionTable
import jakarta.persistence.ElementCollection
import jakarta.persistence.FetchType.EAGER
import jakarta.persistence.JoinColumn
import jakarta.persistence.MappedSuperclass

@MappedSuperclass
abstract class LocalizedModel<T : LocaleModel> : UUIDModel() {
    @ElementCollection(fetch = EAGER)
    @CollectionTable(joinColumns = [JoinColumn(name = "id")])
    val translations: Set<T> = HashSet()

    val translation: T
        get() {
            val language: String = language
            val sorted = translations.sortedBy { it.ordinal }

            return sorted.firstOrNull { it.language == language } ?: sorted.first()
        }
}