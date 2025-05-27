package com.saasant.invoiceServiceSpring.config;


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

@Component
public class TransactionIdFilter implements Filter {
	
	private static final String TRANSACTION_ID_HEADER = "X-Transaction-ID";
    private static final String SESSION_TRANSACTION_ID_KEY = "sessionTransactionId";
    private static final String MDC_KEY = "transactionId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false); 

        String transactionId = null;
        if (session != null) {
            transactionId = (String) session.getAttribute(SESSION_TRANSACTION_ID_KEY);
        }
        if (!StringUtils.hasText(transactionId)) {
            transactionId = httpRequest.getHeader(TRANSACTION_ID_HEADER);
        }
        boolean generatedNew = false;
        if (!StringUtils.hasText(transactionId)) {
            transactionId = UUID.randomUUID().toString();
            generatedNew = true;
        }
        if (session == null) { 
            session = httpRequest.getSession(true); 
        }
        if (generatedNew || httpRequest.getHeader(TRANSACTION_ID_HEADER) != null || session.getAttribute(SESSION_TRANSACTION_ID_KEY) == null) {
             session.setAttribute(SESSION_TRANSACTION_ID_KEY, transactionId);
        }


        MDC.put(MDC_KEY, transactionId);
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_KEY);
        }
    }

}
