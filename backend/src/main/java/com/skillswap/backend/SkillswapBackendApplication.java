package com.skillswap.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class SkillswapBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(SkillswapBackendApplication.class, args);
	}
}