package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
//@EnableWebSocket
public class FitnessTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitnessTrackerApplication.class, args);
	}
}
