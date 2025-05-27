package com.saasant.invoiceServiceSpring.config;

import org.modelmapper.ModelMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import org.modelmapper.ModelMapper;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;

@Configuration
public class ApplicationConfig {
	
	private static final String TRANSACTION_ID_HEADER = "X-Transaction-ID";
    private static final String MDC_KEY = "transactionId"; 

	
	@Bean 
	public RestTemplate restTemplate() { 
		RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
                    throws IOException {
                String transactionId = MDC.get(MDC_KEY);
                if (StringUtils.hasText(transactionId)) {
                    request.getHeaders().add(TRANSACTION_ID_HEADER, transactionId);
                }
                return execution.execute(request, body);
            }
        }));
        return restTemplate;
	}
	
	
    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
