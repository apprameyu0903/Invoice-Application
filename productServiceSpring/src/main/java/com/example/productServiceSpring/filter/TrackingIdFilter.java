package com.example.productServiceSpring.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.UUID;

public class TrackingIdFilter implements Filter {
	private static final String TRANSACTION_ID_HEADER_NAME = "X-Transaction-ID";
    private static final String MDC_TRANSACTION_ID_KEY = "transactionId"; 

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String transactionIdFromHeader = null;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            transactionIdFromHeader = httpRequest.getHeader(TRANSACTION_ID_HEADER_NAME);
        }
        
        if (StringUtils.hasText(transactionIdFromHeader)) {
            MDC.put(MDC_TRANSACTION_ID_KEY, transactionIdFromHeader);
        }
        try {
            chain.doFilter(request, response);
        } finally {
            if (StringUtils.hasText(transactionIdFromHeader)) { 
                MDC.remove(MDC_TRANSACTION_ID_KEY);
            }
        }
    }
}
