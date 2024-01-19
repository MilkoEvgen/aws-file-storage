package com.milko.awsfilestorage.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Bean
    @FlywayDataSource
    public DataSource flywayDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:mysql://db:3306/aws_file_storage");
        dataSource.setUsername("eugene");
        dataSource.setPassword("eugene");
        return dataSource;
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        return Flyway.configure()
                .dataSource(flywayDataSource())
                .locations("classpath:db/migration")
                .load();
    }
}