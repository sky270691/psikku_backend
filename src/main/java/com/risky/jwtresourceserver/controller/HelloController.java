package com.risky.jwtresourceserver.controller;

import com.risky.jwtresourceserver.dto.UserRegisterDto;
import com.risky.jwtresourceserver.entity.TokenFactory;
import com.risky.jwtresourceserver.entity.User;
import com.risky.jwtresourceserver.service.CustomClientTokenService;
import com.risky.jwtresourceserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
public class HelloController {

    @Autowired
    CustomClientTokenService clientTokenService;

    @Autowired
    TokenFactory tf ;

    @Autowired
    UserService userService;

    @GetMapping("/hello")
    public String hello(){
        return "HELLO!";
    }

    @PostMapping("/register")
    public String register(@RequestBody UserRegisterDto userRegisterDto) {
        return userService.registerNewUser(userRegisterDto);

    }


    @PostMapping(value = "/login")
    public TokenFactory successLogin3(@RequestHeader("Authorization") String header) {
        String[] credential = header.split(" ");
        byte[] decoded = Base64.getDecoder().decode(credential[1]);
        String fullCredential = new String(decoded);
        String[] extractedFullCredential = fullCredential.split(":");
        String username = extractedFullCredential[0];
        String password = extractedFullCredential[1];
        return userService.loginExistingUser(username,password);
    }


}
