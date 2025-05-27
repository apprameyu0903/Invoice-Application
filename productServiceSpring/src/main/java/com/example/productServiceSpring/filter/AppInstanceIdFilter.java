package com.example.productServiceSpring.filter; // Or your config package

import com.example.productServiceSpring.ProductServiceSpringApplication;
import jakarta.servlet.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component; // If using component scanning

import java.io.IOException;

@Component // Or register via FilterRegistrationBean
public class AppInstanceIdFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            MDC.put(ProductServiceSpringApplication.MDC_APP_INSTANCE_ID_KEY, ProductServiceSpringApplication.APPLICATION_INSTANCE_ID);
            chain.doFilter(request, response);
        } finally {
            MDC.remove(ProductServiceSpringApplication.MDC_APP_INSTANCE_ID_KEY);
        }
    }
}