package com.effectivemobile.practice3.config;

import com.effectivemobile.practice3.config.properties.PostgresCredentials;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

@Configuration
@EnableConfigurationProperties({R2dbcProperties.class, FlywayProperties.class})
@RequiredArgsConstructor
public class DatabaseConfig {

    private final PostgresCredentials postgresCredentials;

    @Bean(initMethod = "migrate")
    public Flyway flyway(FlywayProperties flywayProperties, R2dbcProperties r2dbcProperties) {
        return Flyway.configure()
                .dataSource(
                        flywayProperties.getUrl(),
                        r2dbcProperties.getUsername(),
                        r2dbcProperties.getPassword()
                )
                .locations(flywayProperties.getLocations().toArray(String[]::new))
                .baselineOnMigrate(true)
                .load();
    }

    @Bean
    public ConnectionFactory connectionFactory() {

        // postgres
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host("localhost")
                        .port(5432)
                        .database("rest")
                        .username(postgresCredentials.getUsername())
                        .password(postgresCredentials.getPassword())
                        .build()
        );
    }

    @Bean
    DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
        return DatabaseClient.builder()
                .connectionFactory(connectionFactory)
                .namedParameters(true)
                .build();
    }

}