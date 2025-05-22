package com.saasant.firstSpringProject.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.UUID;
import com.saasant.firstSpringProject.FirstSpringProjectApplication;

@Component
public class MdcLoggingFilter implements Filter{
	
	private static final String SESSION_TRACKING_ID_KEY = "sessionTrackingId"; 
    private static final String MDC_TRACKING_ID_KEY = "trackingId"; 

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
    	String trackingId = null;

        // Ensure we are dealing with an HTTP request to access session information.
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpSession session = httpRequest.getSession(true);
            Object sessionAttr = session.getAttribute(SESSION_TRACKING_ID_KEY);
            if (sessionAttr instanceof String) {
                trackingId = (String) sessionAttr;
            }
            if (trackingId == null || trackingId.trim().isEmpty()) {
                trackingId = UUID.randomUUID().toString();
                session.setAttribute(SESSION_TRACKING_ID_KEY, trackingId);
               
            }
            MDC.put(MDC_TRACKING_ID_KEY, trackingId);
        } 
        try {

            filterChain.doFilter(request, response);
        } finally {
            if (trackingId != null) { 
                MDC.remove(MDC_TRACKING_ID_KEY);
            }
        }
    }

}

