package com.example.BikeChat;

import com.example.BikeChat.User.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BikeChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(BikeChatApplication.class, args);
	}

	User user = new User((long) 1, "testUsername", "test@email", "@fgq", "someUrl", "hello");

}
