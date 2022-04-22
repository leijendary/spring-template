package com.leijendary.spring.boot.template.core.model

import com.leijendary.spring.boot.template.core.util.RequestContext.language
import java.util.*
import javax.persistence.*

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
            val sorted = translations.sortedBy { it!!.ordinal }

            return sorted.first { it!!.language == language } ?: sorted.first()
        }
}