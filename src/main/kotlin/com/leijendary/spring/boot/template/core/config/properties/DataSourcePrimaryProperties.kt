package com.leijendary.spring.boot.template.core.config.properties

import com.zaxxer.hikari.HikariConfig
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.datasource.primary")
class DataSourcePrimaryProperties : HikariConfig()