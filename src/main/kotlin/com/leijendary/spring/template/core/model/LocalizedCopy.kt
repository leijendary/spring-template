package com.leijendary.spring.template.core.model

import com.leijendary.spring.template.core.util.RequestContext.language
import java.util.*
import jakarta.persistence.*

@MappedSuperclass
abstract class LocalizedCopy<T : LocaleCopy> : AppModel() {
    @Id
    var id: UUID? = null

    @ElementCollection
    @CollectionTable(joinColumns = [JoinColumn(name = "id")])
    val translations: Set<T> = HashSet()

    val translation: T
        get() {
            val language: String = language
            val sorted = translations.sortedBy { it.ordinal }

            return sorted.firstOrNull { it.language == language } ?: sorted.first()
        }
}