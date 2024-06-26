package com.leijendary.extension

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.MathContext.DECIMAL32
import java.math.RoundingMode
import java.math.RoundingMode.HALF_UP

val ONE_HUNDRED = 100.toBigDecimal()

fun BigDecimal.percent(value: BigDecimal): BigDecimal {
    if (this.compareTo(ZERO) == 0 || value.compareTo(ZERO) == 0) {
        return ZERO
    }

    return (this * value).divide(ONE_HUNDRED, DECIMAL32)
}

fun BigDecimal.scaled(scale: Int = 2, mode: RoundingMode = HALF_UP): BigDecimal {
    return setScale(scale, mode)
}
