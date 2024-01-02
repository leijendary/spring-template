package com.leijendary.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.datasource.primary")
class DataSourcePrimaryProperties {
    var name: String = "Primary"
    lateinit var url: String
    var poolSize: Int = 10
}
