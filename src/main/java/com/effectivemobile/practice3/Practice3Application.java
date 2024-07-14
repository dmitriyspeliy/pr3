package com.effectivemobile.practice3;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@OpenAPIDefinition
@EnableWebFlux
public class Practice3Application {

	public static void main(String[] args) {
		SpringApplication.run(Practice3Application.class, args);
	}

}
