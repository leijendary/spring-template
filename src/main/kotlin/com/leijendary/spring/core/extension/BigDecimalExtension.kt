package com.leijendary.spring.core.extension

import com.leijendary.spring.core.util.BeanContainer.numberProperties
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

private val ONE_HUNDRED = BigDecimal(100)

fun BigDecimal.scaled(): BigDecimal {
    return setScale(numberProperties.scale, numberProperties.round)
}

fun BigDecimal.percent(value: BigDecimal): BigDecimal {
    if (this == ZERO || value == ZERO) {
        return ZERO
    }

    return this * value / ONE_HUNDRED
}
