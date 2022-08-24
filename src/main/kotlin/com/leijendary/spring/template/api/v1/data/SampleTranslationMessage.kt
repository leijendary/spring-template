package com.leijendary.spring.template.api.v1.data

import com.leijendary.spring.template.core.data.LocaleData

data class SampleTranslationMessage(
    val name: String,
    val description: String,
    override val language: String,
    override val ordinal: Int
) : LocaleData(language, ordinal)