package com.leijendary.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.datasource.primary")
class DataSourcePrimaryProperties {
    var name = "Primary"
    lateinit var url: String
    var poolSize = 10
}
