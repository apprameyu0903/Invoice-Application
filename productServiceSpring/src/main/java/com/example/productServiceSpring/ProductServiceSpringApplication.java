package com.example.productServiceSpring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductServiceSpringApplication {
	
	public static final String APPLICATION_INSTANCE_ID = java.util.UUID.randomUUID().toString();
	public static final String MDC_APP_INSTANCE_ID_KEY = "trackingId";

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceSpringApplication.class, args);
	}

}
