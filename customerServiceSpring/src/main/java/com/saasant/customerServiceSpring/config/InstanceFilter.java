package com.saasant.customerServiceSpring.config;

import java.io.IOException;


import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import com.saasant.customerServiceSpring.CustomerServiceSpringApplication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.Filter;
import org.slf4j.MDC;

@Component
public class InstanceFilter implements Filter {
	
	private static final String LOGGING_ID_KEY = "txId";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
	        throws IOException, ServletException {
		try {
	        MDC.put(LOGGING_ID_KEY, CustomerServiceSpringApplication.APPLICATION_INSTANCE_ID);
	        filterChain.doFilter(request, response);
	    } finally {
	        MDC.remove(LOGGING_ID_KEY);
	    }
	}

}

