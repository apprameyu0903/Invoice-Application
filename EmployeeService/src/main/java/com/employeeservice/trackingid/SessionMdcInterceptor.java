package com.employeeservice.trackingid;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.UUID;


@Component
public class SessionMdcInterceptor implements HandlerInterceptor {

	private static final String TRANSACTION_ID_HEADER_NAME = "X-Transaction-ID";
    private static final String MDC_TRANSACTION_ID_KEY = "transactionId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        
        String transactionIdFromHeader = request.getHeader(TRANSACTION_ID_HEADER_NAME);

        if (StringUtils.hasText(transactionIdFromHeader)) {
            MDC.put(MDC_TRANSACTION_ID_KEY, transactionIdFromHeader);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String transactionIdFromHeader = request.getHeader(TRANSACTION_ID_HEADER_NAME);
        if (StringUtils.hasText(transactionIdFromHeader)) {
             MDC.remove(MDC_TRANSACTION_ID_KEY);
        }
    }
}








