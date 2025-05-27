package com.saasant.customerServiceSpring.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.UUID;
import com.saasant.customerServiceSpring.CustomerServiceSpringApplication;

@Component
public class MdcLoggingFilter implements Filter{
	
	private static final String SESSION_TRACKING_ID_KEY = "sessionTrackingId"; 
    private static final String MDC_TRACKING_ID_KEY = "trackingId"; 
    private static final String TRANSACTION_ID_HEADER = "X-Transaction-ID";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
    	String finalTransactionId = null;

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            
            // 1. Try to get transaction ID from incoming header
            String headerTransactionId = httpRequest.getHeader(TRANSACTION_ID_HEADER);

            if (StringUtils.hasText(headerTransactionId)) {
                finalTransactionId = headerTransactionId;
            } else {
                // 2. Fallback to session or generate new if not in header
                HttpSession session = httpRequest.getSession(true); // Get or create session
                Object sessionAttr = session.getAttribute(SESSION_TRACKING_ID_KEY);
                if (sessionAttr instanceof String && StringUtils.hasText((String)sessionAttr)) {
                    finalTransactionId = (String) sessionAttr;
                } else {
                    finalTransactionId = UUID.randomUUID().toString();
                    session.setAttribute(SESSION_TRACKING_ID_KEY, finalTransactionId);
                }
            }
            MDC.put(MDC_TRACKING_ID_KEY, finalTransactionId);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            if (finalTransactionId != null) {
                MDC.remove(MDC_TRACKING_ID_KEY);
            }
        }
    }

}

