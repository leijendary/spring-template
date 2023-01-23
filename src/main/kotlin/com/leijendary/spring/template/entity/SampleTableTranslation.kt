package com.leijendary.spring.template.entity

import com.leijendary.spring.template.core.entity.LocaleEntity
import jakarta.persistence.Embeddable

@Embeddable
data class SampleTableTranslation(
    var name: String,
    var description: String,
    override var language: String,
    override var ordinal: Int
) : LocaleEntity(language, ordinal)
