package com.psikku.backend.controller;

import com.psikku.backend.dto.UserRegisterDto;
import com.psikku.backend.dto.UserRegisterResponse;
import com.psikku.backend.entity.TokenFactory;
import com.psikku.backend.service.CustomClientTokenService;
import com.psikku.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Base64;

@RestController
@RequestMapping("/users")
public class HelloController {

    @Autowired
    CustomClientTokenService clientTokenService;

    @Autowired
    UserService userService;

    @GetMapping("/hello")
    public String hello(){
        return "HELLO!";
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> register(@RequestBody UserRegisterDto userRegisterDto) {
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
        try {
            return userService.loginExistingUser(username,password);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "incorrect username or password",e);
        }
    }


}
