package com.risky.jwtresourceserver.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.risky.jwtresourceserver.entity.TokenFactory;
import com.risky.jwtresourceserver.service.CustomClientTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;

@RestController
public class HelloController {


    @Autowired
    TokenFactory tf ;

    @Autowired
    CustomClientTokenService client;

    @GetMapping("/hello")
    public String hello(){
        return "HELLO!";
    }

    @PostMapping("/register")
    public String register(HttpServletResponse response) {

        String uri = "http://localhost:8080/oauth/token?scope=write&grant_type=password&username=john&password=12345";
        response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        response.setHeader("Location", uri);
        return "";
    }

    @PostMapping(value = "/success")
    public TokenFactory successLogin(){
        return client.getToken();
    }
}
