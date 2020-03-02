package com.risky.jwtresourceserver.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.risky.jwtresourceserver.entity.TokenFactory;
import com.risky.jwtresourceserver.service.CustomClientTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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
import javax.xml.bind.DatatypeConverter;
import java.net.HttpURLConnection;
import java.util.Base64;
import java.util.Date;

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

//    @PostMapping("/register")
//    public String register() {
//    }

//    @GetMapping(value = "/check/{token}")
//    public String check_token(@PathVariable String token){
//
//
//        String key  = Base64.getEncoder().encodeToString("12345".getBytes());
//        Claims jws = Jwts.parser().setSigningKey(key)
//                    .parseClaimsJws(token)
//                    .getBody();
//
//  String[] jwsString = jws.toString().split(",");
//        String username = jwsString[1].substring(11);
//        return username;
//    }



    @PostMapping(value = "/login")
    public TokenFactory succesLogin(){
        return client.getToken();
    }

}
