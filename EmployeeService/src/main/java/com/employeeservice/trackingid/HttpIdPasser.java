package com.employeeservice.trackingid;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import java.io.IOException;
import com.employeeservice.EmployeeServiceApplication;

@Component
public class HttpIdPasser implements Filter{
	
	private static final String LOGGING_ID_KEY = "txId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
    	try {
            MDC.put(LOGGING_ID_KEY, EmployeeServiceApplication.APPLICATION_INSTANCE_ID);
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(LOGGING_ID_KEY);
        }
    }

}

