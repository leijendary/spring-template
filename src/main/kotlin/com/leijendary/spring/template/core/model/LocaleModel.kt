package com.leijendary.spring.template.core.model

import java.io.Serializable

abstract class LocaleModel(open val language: String, open val ordinal: Int) : Serializable
