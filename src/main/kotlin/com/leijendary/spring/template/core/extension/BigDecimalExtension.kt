package com.leijendary.spring.template.core.extension

import com.leijendary.spring.template.core.config.properties.NumberProperties
import com.leijendary.spring.template.core.util.SpringContext.Companion.getBean
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.BigDecimal.valueOf
import java.math.RoundingMode

private val numberProperties = getBean(NumberProperties::class)
private val oneHundred = valueOf(100)

fun BigDecimal.scaled(): BigDecimal = this.setScale(numberProperties.scale, numberProperties.round)

fun BigDecimal.percent(
    value: BigDecimal,
    scale: Int = numberProperties.scale,
    roundingMode: RoundingMode = numberProperties.round
): BigDecimal {
    return if (this.compareTo(ZERO) == 0 || value.compareTo(ZERO) == 0) {
        ZERO
    } else {
        this
            .multiply(value)
            .divide(oneHundred, scale, roundingMode)
    }
}
