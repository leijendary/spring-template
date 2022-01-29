package com.leijendary.spring.boot.template.model

import com.leijendary.spring.boot.template.core.model.LocaleModel
import javax.persistence.Embeddable

@Embeddable
data class SampleTableTranslations(
    var name: String,
    var description: String,
    override var language: String,
    override var ordinal: Int
) : LocaleModel(language, ordinal)