package com.leijendary.spring.boot.template.core.util

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.BigDecimal.valueOf
import java.math.RoundingMode
import java.math.RoundingMode.HALF_UP

const val SCALE = 4
val ONE_HUNDRED: BigDecimal = valueOf(100)
val ROUNDING_MODE = HALF_UP

fun BigDecimal.percent(value: BigDecimal, scale: Int = SCALE, roundingMode: RoundingMode = ROUNDING_MODE): BigDecimal {
    return if (this.compareTo(ZERO) == 0 || value.compareTo(ZERO) == 0) {
        ZERO
    } else this.multiply(value).divide(ONE_HUNDRED, scale, roundingMode)
}