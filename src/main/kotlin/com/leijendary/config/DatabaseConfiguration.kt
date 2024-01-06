package com.leijendary.config

import com.leijendary.config.properties.DataSourcePrimaryProperties
import com.leijendary.config.properties.DataSourceReadOnlyProperties
import com.zaxxer.hikari.HikariDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import javax.sql.DataSource

@Configuration
class DatabaseConfiguration {
    @Bean
    @Primary
    fun dataSource(primaryDataSource: DataSource, readOnlyDataSource: DataSource): DataSource {
        return LazyConnectionDataSourceProxy(primaryDataSource).apply {
            setReadOnlyDataSource(readOnlyDataSource)
        }
    }

    @Bean
    fun primaryDataSource(config: DataSourcePrimaryProperties): DataSource {
        return HikariDataSource(config)
    }

    @Bean
    fun readOnlyDataSource(config: DataSourceReadOnlyProperties): DataSource {
        return HikariDataSource(config)
    }
}
