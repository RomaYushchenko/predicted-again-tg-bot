package com.ua.yushchenko.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Configuration class for database connection.
 * Uses environment variables from .env file for database credentials.
 *
 * @author AI
 * @version 0.1-beta
 */
@Configuration
public class DatabaseConfig {

    /**
     * Creates and configures the DataSource bean using environment variables.
     *
     * @param dotenv Dotenv instance for accessing environment variables
     * @return configured DataSource
     */
    @Bean
    public DataSource dataSource(Dotenv dotenv) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(dotenv.get("DB_URL"));
        dataSource.setUsername(dotenv.get("DB_USERNAME"));
        dataSource.setPassword(dotenv.get("DB_PASSWORD"));
        return dataSource;
    }
} 