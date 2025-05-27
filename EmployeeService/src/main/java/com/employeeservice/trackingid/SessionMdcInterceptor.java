package com.employeeservice.trackingid;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;


@Component
public class SessionMdcInterceptor implements HandlerInterceptor {

	private static final String TRANSACTION_ID_HEADER_NAME = "X-Transaction-ID";
    private static final String MDC_TRANSACTION_ID_KEY = "transactionId"; 
    private static final String SESSION_ID_KEY = "sessionTxId"; 

    @Override
    public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler) {
        
        String finalTransactionId = null;
        String headerTransactionId = request.getHeader(TRANSACTION_ID_HEADER_NAME);

        if (StringUtils.hasText(headerTransactionId)) {
            finalTransactionId = headerTransactionId;
        } else {
            HttpSession session = request.getSession(true);
            finalTransactionId = (String) session.getAttribute(SESSION_ID_KEY);
            if (!StringUtils.hasText(finalTransactionId)) {
                finalTransactionId = UUID.randomUUID().toString();
                session.setAttribute(SESSION_ID_KEY, finalTransactionId);
            }
        }
        
        MDC.put(MDC_TRANSACTION_ID_KEY, finalTransactionId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,HttpServletResponse response,Object handler,Exception ex) {
        MDC.remove(MDC_TRANSACTION_ID_KEY);
    }
}








