package com.effectivemobile.practice3.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.r2dbc")
public class PostgresCredentials {
    private String username;
    private String password;
    private String host;
    private Integer port;
    private String databaseName;
}

