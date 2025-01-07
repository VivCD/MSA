package com.example.BikeChat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.BikeChat"})
public class BikeChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(BikeChatApplication.class, args);

	}
}
