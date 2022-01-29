package com.leijendary.spring.boot.template.api.v1.data

import com.leijendary.spring.boot.template.core.data.LocaleData

data class SampleTranslationMessage(
    val name: String,
    val description: String,
    override val language: String,
    override val ordinal: Int
) : LocaleData(language, ordinal)