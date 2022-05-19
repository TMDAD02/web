package com.chatapp.web;

import com.chatapp.web.services.ServicioAlmacenamiento;
import com.chatapp.web.services.ServicioTrending;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WebApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}

	@Bean
	CommandLineRunner init(ServicioAlmacenamiento storageService, ServicioTrending servicioTrending) {
		return (args) -> {
			storageService.init();
			servicioTrending.init();
		};
	}
}
