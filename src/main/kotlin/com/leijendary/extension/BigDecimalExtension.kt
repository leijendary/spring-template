package com.leijendary.extension

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.RoundingMode
import java.math.RoundingMode.DOWN

private val ONE_HUNDRED = BigDecimal(100)

fun BigDecimal.scaled(scale: Int = 2, mode: RoundingMode = DOWN): BigDecimal {
    return setScale(scale, mode)
}

fun BigDecimal.percent(value: BigDecimal): BigDecimal {
    if (this == ZERO || value == ZERO) {
        return ZERO
    }

    return this * value / ONE_HUNDRED
}
