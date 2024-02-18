package com.ep.account.datasource

import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import javax.sql.DataSource


@Configuration
class DataSourceConfig {

    @Bean
    @Profile("monolithic")
    fun monolithicDatasource(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java)
            .url("jdbc:h2:mem:epdb1")
            .username("root")
            .password("root")
            .build()
    }

    @Bean
    @Profile("deposit")
    fun ep1Datasource(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java)
            .url("jdbc:h2:mem:epdb1")
            .username("root")
            .password("root")
            .build()
    }

    @Bean
    @Profile("withdraw")
    fun ep2Datasource(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java)
            .url("jdbc:h2:mem:epdb2")
            .username("root")
            .password("root")
            .build()
    }
}