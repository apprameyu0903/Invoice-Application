package com.saasant.customerServiceSpring;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.saasant.customerServiceSpring.CustomerServiceSpringApplication;

@SpringBootApplication
public class CustomerServiceSpringApplication {
	
	private static final Logger log = LoggerFactory.getLogger(CustomerServiceSpringApplication.class);


	public static void main(String[] args) {
        log.info("Starting FirstSpringProjectApplication");
		SpringApplication.run(CustomerServiceSpringApplication.class, args);
		log.info("FirstSpringProjectApplication has started successfully");
	}
	
    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

}
