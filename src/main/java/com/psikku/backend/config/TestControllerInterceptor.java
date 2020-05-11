package com.psikku.backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TestControllerInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(TestControllerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        logger.info("| Request from:'"+request.getRemoteAddr()+"' | Endpoint:'"+request.getRequestURI()+"' |");

        return true;
    }
}
