package com.leijendary.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
class DatabaseConfiguration {
    @Bean
    @Primary
    fun dataSource(primaryDataSource: DataSource, readOnlyDataSource: DataSource): DataSource {
        return LazyConnectionDataSourceProxy(primaryDataSource).apply {
            setReadOnlyDataSource(readOnlyDataSource)
        }
    }

    @Bean
    @ConfigurationProperties("spring.datasource")
    fun primaryDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Bean
    @ConfigurationProperties("spring.datasource.read-only")
    fun readOnlyDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }
}
