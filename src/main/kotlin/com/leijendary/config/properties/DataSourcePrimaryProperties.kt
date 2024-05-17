package com.leijendary.config.properties

import com.zaxxer.hikari.HikariConfig
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.datasource.primary", ignoreUnknownFields = false)
class DataSourcePrimaryProperties : HikariConfig()
