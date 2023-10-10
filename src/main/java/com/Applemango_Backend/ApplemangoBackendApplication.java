package com.Applemango_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class ApplemangoBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApplemangoBackendApplication.class, args);
	}

}
