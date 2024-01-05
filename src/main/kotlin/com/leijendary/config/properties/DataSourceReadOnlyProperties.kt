package com.leijendary.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.datasource.read-only")
class DataSourceReadOnlyProperties {
    var name = "Read-only"
    lateinit var url: String
    var poolSize = 10
}
