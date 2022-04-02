package com.chatapp.web;

import com.chatapp.web.services.ServicioAlmacenamiento;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}

	@Bean
	CommandLineRunner init(ServicioAlmacenamiento storageService) {
		return (args) -> {
			//storageService.deleteAll();
			storageService.init();
		};
	}
}
