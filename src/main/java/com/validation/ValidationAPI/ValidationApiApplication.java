package com.validation.ValidationAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ValidationApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ValidationApiApplication.class, args);
	}

}
