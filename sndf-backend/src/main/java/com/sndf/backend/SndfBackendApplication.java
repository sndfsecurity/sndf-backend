package com.sndf.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SndfBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SndfBackendApplication.class, args);
		

	}

}
