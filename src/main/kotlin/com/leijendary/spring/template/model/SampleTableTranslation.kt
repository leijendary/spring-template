package com.leijendary.spring.template.model

import com.leijendary.spring.template.core.model.LocaleModel
import jakarta.persistence.Embeddable

@Embeddable
data class SampleTableTranslation(
    var name: String,
    var description: String,
    override var language: String,
    override var ordinal: Int
) : LocaleModel(language, ordinal)
