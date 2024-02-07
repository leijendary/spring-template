package com.leijendary.extension

import com.leijendary.util.numberProperties
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.RoundingMode

private val ONE_HUNDRED = BigDecimal(100)

fun BigDecimal.scaled(roundingMode: RoundingMode = numberProperties.round): BigDecimal {
    return setScale(numberProperties.scale, roundingMode)
}

fun BigDecimal.percent(value: BigDecimal): BigDecimal {
    if (this == ZERO || value == ZERO) {
        return ZERO
    }

    return this * value / ONE_HUNDRED
}
