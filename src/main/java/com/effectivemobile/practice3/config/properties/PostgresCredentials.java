package com.effectivemobile.practice3.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.r2dbc")
public class PostgresCredentials {

    String username;
    String password;

}

