package com.leijendary.spring.template.core.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.math.RoundingMode
import java.math.RoundingMode.HALF_UP

@ConfigurationProperties(prefix = "number")
class NumberProperties {
    var scale: Int = 2
    var round: RoundingMode = HALF_UP
}