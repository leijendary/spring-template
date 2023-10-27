package com.leijendary.spring.core.config.properties

import com.zaxxer.hikari.HikariConfig
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.datasource.primary")
class DataSourcePrimaryProperties : HikariConfig()
