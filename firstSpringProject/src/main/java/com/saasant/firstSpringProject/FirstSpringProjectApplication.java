package com.saasant.firstSpringProject;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.UUID;

@SpringBootApplication
public class FirstSpringProjectApplication {
	
	private static final Logger log = LoggerFactory.getLogger(FirstSpringProjectApplication.class);

	public static void main(String[] args) {
        log.info("Starting FirstSpringProjectApplication");
		SpringApplication.run(FirstSpringProjectApplication.class, args);
		log.info("FirstSpringProjectApplication has started successfully");
	}
	
    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
