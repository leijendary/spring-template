package com.leijendary.spring.template.api.v1.model

import com.leijendary.spring.template.core.model.LocaleModel

data class SampleTranslationResponse(
    val name: String,
    val description: String,
    override val language: String,
    override val ordinal: Int
) : LocaleModel(language, ordinal)
