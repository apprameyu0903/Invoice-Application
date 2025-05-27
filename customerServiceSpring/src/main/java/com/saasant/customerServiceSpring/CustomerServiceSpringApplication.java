package com.saasant.customerServiceSpring;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.saasant.customerServiceSpring.CustomerServiceSpringApplication;
import com.saasant.customerServiceSpring.config.AuditAwareImpl;

@SpringBootApplication
@EnableJpaAuditing
public class CustomerServiceSpringApplication {
	
	private static final Logger log = LoggerFactory.getLogger(CustomerServiceSpringApplication.class);
    public static final String APPLICATION_INSTANCE_ID = UUID.randomUUID().toString();
    private static final String LOGGING_ID_KEY = "txId";


	public static void main(String[] args) {
		if (MDC.get(LOGGING_ID_KEY) == null) {
            MDC.put(LOGGING_ID_KEY, APPLICATION_INSTANCE_ID);
        }

		log.info("Starting CustomerServiceSpringApplication with Instance ID: {}", APPLICATION_INSTANCE_ID);
		SpringApplication.run(CustomerServiceSpringApplication.class, args);
		log.info("CustomerServiceSpringApplication has started successfully with Instance ID: {}", APPLICATION_INSTANCE_ID);
	}
	


}
