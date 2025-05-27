package com.saasant.customerServiceSpring.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class ApplicationConfig {
	
    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
    
    @Bean
    public AuditorAware<String> auditorProvider() { //
        return new AuditAwareImpl();
    }

}
