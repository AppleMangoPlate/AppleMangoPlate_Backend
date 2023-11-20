package com.Applemango_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ApplemangoBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApplemangoBackendApplication.class, args);
	}

}
