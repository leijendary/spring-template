package com.leijendary.spring.template.core.config.properties

import com.zaxxer.hikari.HikariConfig
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.datasource.readonly")
class DataSourceReadonlyProperties : HikariConfig()