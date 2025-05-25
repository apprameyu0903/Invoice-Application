package com.employeeservice.trackingid;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;


@Component
public class SessionMdcInterceptor implements HandlerInterceptor {

    private static final String SESSION_ID_KEY = "sessionTxId";
    private static final String MDC_KEY = "trackingId";

    @Override
    public boolean preHandle(HttpServletRequest request, 
                           HttpServletResponse response, 
                           Object handler) {
        
        // Get or create session (false = don't create if doesn't exist)
        HttpSession session = request.getSession(true);
        
        if (session != null) {
            // Get existing session ID or create new one
            String sessionId = (String) session.getAttribute(SESSION_ID_KEY);
            if (sessionId == null) {
                sessionId = UUID.randomUUID().toString();
                session.setAttribute(SESSION_ID_KEY, sessionId);
            }
            
            // Put in MDC
            MDC.put(MDC_KEY, sessionId);
        }
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, 
                              HttpServletResponse response, 
                              Object handler, 
                              Exception ex) {
        // Clear MDC to prevent memory leaks
        MDC.remove(MDC_KEY);
    }
}








//<dependency>
//<groupId>javax.servlet</groupId>
//<artifactId>javax.servlet-api</artifactId>
//<version>4.0.1</version>
//<scope>provided</scope>
//</dependency>
