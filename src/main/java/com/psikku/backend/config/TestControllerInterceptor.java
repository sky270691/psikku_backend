package com.psikku.backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TestControllerInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(TestControllerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String username = "";
        if(SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null){
            username = SecurityContextHolder.getContext().getAuthentication().getName();
        }

        logger.info("| Request from:'"+request.getRemoteAddr()+"'| with-username:'"+username+"' | Endpoint:'"+request.getRequestURI()+"' |");

        return true;
    }
}
