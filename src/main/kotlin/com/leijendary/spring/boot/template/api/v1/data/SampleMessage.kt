package com.leijendary.spring.boot.template.api.v1.data

import com.leijendary.spring.boot.template.core.data.LocalizedData

data class SampleMessage(val column1: String, val column2: Int) : LocalizedData<SampleTranslationMessage>()