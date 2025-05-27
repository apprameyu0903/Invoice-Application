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

        String finalTransactionId = null;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String headerTransactionId = httpRequest.getHeader(TRANSACTION_ID_HEADER_NAME);

            if (StringUtils.hasText(headerTransactionId)) {
                finalTransactionId = headerTransactionId;
            } else {
                finalTransactionId = UUID.randomUUID().toString(); // Fallback if no header
            }
        } else {
            finalTransactionId = UUID.randomUUID().toString(); // For non-HTTP requests, though less common in this context
        }
        
        MDC.put(MDC_TRANSACTION_ID_KEY, finalTransactionId);
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_TRANSACTION_ID_KEY);
        }
    }
}
