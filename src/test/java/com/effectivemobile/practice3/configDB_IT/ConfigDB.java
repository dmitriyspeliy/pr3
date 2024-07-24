package com.effectivemobile.practice3.configDB_IT;

import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest
@AutoConfigureCache
@DirtiesContext
@ComponentScan(basePackages = {"com.effectivemobile.practice3.repository", "com.effectivemobile.practice3.service", "com.effectivemobile.practice3.mapper"
        , "com.effectivemobile.practice3.controller"})
public class ConfigDB {
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:12"));

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", ConfigDB::jdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.baselineOnMigrate", () -> true);
        registry.add("spring.flyway.enabled", () -> true);
    }

    private static String jdbcUrl() {
        return String.format("jdbc:postgresql://%s:%s/%s",
                postgres.getContainerIpAddress(),
                postgres.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                postgres.getDatabaseName());
    }
}