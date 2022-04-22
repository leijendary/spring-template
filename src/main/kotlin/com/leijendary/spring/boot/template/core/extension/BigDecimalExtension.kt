package com.leijendary.spring.boot.template.core.extension

import com.leijendary.spring.boot.template.core.config.properties.NumberProperties
import com.leijendary.spring.boot.template.core.util.SpringContext.Companion.getBean
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.BigDecimal.valueOf
import java.math.RoundingMode

private val numberProperties = getBean(NumberProperties::class.java)
private val SCALE = numberProperties.scale
private val ROUNDING_MODE = numberProperties.round
private val ONE_HUNDRED: BigDecimal = valueOf(100)

fun BigDecimal.scaled(): BigDecimal {
    return this.setScale(SCALE, ROUNDING_MODE)
}

fun BigDecimal.percent(value: BigDecimal, scale: Int = SCALE, roundingMode: RoundingMode = ROUNDING_MODE): BigDecimal {
    return if (this.compareTo(ZERO) == 0 || value.compareTo(ZERO) == 0) {
        ZERO
    } else this.multiply(value).divide(ONE_HUNDRED, scale, roundingMode)
}