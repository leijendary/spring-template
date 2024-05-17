package com.leijendary.config.properties

import com.zaxxer.hikari.HikariConfig
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.datasource.read-only", ignoreUnknownFields = false)
class DataSourceReadOnlyProperties : HikariConfig()
