package com.leijendary.spring.boot.template.core.config;

import static com.leijendary.spring.boot.template.core.config.DatabaseConfiguration.DataSourceType.PRIMARY;
import static com.leijendary.spring.boot.template.core.config.DatabaseConfiguration.DataSourceType.READONLY;
import static org.springframework.transaction.support.TransactionSynchronizationManager.isCurrentTransactionReadOnly;

import java.util.HashMap;

import javax.sql.DataSource;

import com.leijendary.spring.boot.template.core.config.properties.DataSourcePrimaryProperties;
import com.leijendary.spring.boot.template.core.config.properties.DataSourceReadonlyProperties;
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DatabaseConfiguration {

    private final DataSourcePrimaryProperties primaryProperties;
    private final DataSourceReadonlyProperties readonlyProperties;

    public enum DataSourceType {
        PRIMARY,
        READONLY;
    }

    public class TransactionRoutingDataSource extends AbstractRoutingDataSource {

        @Override
        protected Object determineCurrentLookupKey() {
            final var isReadOnly = isCurrentTransactionReadOnly();

            return isReadOnly ? READONLY : PRIMARY;
        }
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(routingDataSource());
    }

    @Bean
    public TransactionRoutingDataSource routingDataSource() {
        final var routing = new TransactionRoutingDataSource();
        final var dataSource = new HashMap<>();
        dataSource.put(PRIMARY, primaryDataSource());
        dataSource.put(READONLY, readonlyDataSource());

        routing.setTargetDataSources(dataSource);

        return routing;
    }

    @Bean
    public HikariDataSource primaryDataSource() {
        return new HikariDataSource(primaryProperties);
    }

    @Bean
    public HikariDataSource readonlyDataSource() {
        return new HikariDataSource(readonlyProperties);
    }
}
