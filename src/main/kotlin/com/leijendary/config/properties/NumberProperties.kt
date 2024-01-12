package com.leijendary.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.math.RoundingMode
import java.math.RoundingMode.HALF_UP

@ConfigurationProperties("number")
@JvmRecord
data class NumberProperties(val scale: Int = 2, val round: RoundingMode = HALF_UP)
