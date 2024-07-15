package com.effectivemobile.practice3;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@OpenAPIDefinition
@EnableWebFlux
@EnableCaching
@EnableScheduling
@ConfigurationPropertiesScan(basePackages = "com.effectivemobile.practice3.config.properties")
public class Practice3Application {

	public static void main(String[] args) {
		SpringApplication.run(Practice3Application.class, args);
	}

}
