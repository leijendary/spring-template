package com.leijendary.spring.template.core.data

import java.io.Serializable

abstract class LocaleData(open val language: String, open val ordinal: Int) : Serializable