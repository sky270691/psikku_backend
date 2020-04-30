package com.psikku.backend.controller;

import com.psikku.backend.dto.user.UserDto;
import com.psikku.backend.dto.user.UserRegisterDto;
import com.psikku.backend.dto.user.UserRegisterResponse;
import com.psikku.backend.entity.TokenFactory;
import com.psikku.backend.entity.User;
import com.psikku.backend.service.CustomClientTokenService;
import com.psikku.backend.service.TokenService;
import com.psikku.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    TokenService clientTokenService;

    @Autowired
    UserService userService;

    private TokenFactory tokenFactory;

    @GetMapping("/hello")
    public String hello(){
        return "HELLO!";
    }

    @GetMapping
    public List<UserDto> getAllUser(){
        List<User> userList = userService.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for(User user : userList){
            userDtoList.add(userService.convertToUserDto(user));
        }
        return userDtoList;
    }

    @PostMapping(value = "/register", consumes = "multipart/form-data")
    public ResponseEntity<UserRegisterResponse> register(@Valid UserRegisterDto userRegisterDto) {
        System.out.println(userRegisterDto);
        return userService.registerNewUserToAuthServer(userRegisterDto);
    }
//
//    @PostMapping(value = "/register")
//    public ResponseEntity<UserRegisterResponse> register(@Valid @RequestBody UserRegisterDto userRegisterDto) {
//        System.out.println(userRegisterDto);
//        return userService.registerNewUserToAuthServer(userRegisterDto);
//    }

    @PostMapping(value = "/testuser2", consumes = "multipart/form-data")
    public ResponseEntity<UserRegisterResponse> testUser2(UserRegisterResponse userRegisterResponse){
        return new ResponseEntity<>(userRegisterResponse,HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public TokenFactory login (@RequestPart String username, @RequestPart String password){
        try{
            TokenFactory tokenFactory = userService.loginExistingUser(username,password);
            return tokenFactory;
        }catch (RuntimeException e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "incorrect username or password",e);
        }
    }


    @PostMapping("/loginV2")
    public TokenFactory loginV2(@RequestHeader("Authorization") String header) {
        String[] credentialWithBasic = header.split(" ");
        byte[] decoded = Base64.getDecoder().decode(credentialWithBasic[1]);
        String fullCredential = new String(decoded);
        String[] extractedFullCredential = fullCredential.split(":");
        String username = extractedFullCredential[0];
        String password = extractedFullCredential[1];
        try {
            TokenFactory tokenFactory = userService.loginExistingUser(username,password);
//            String userNameFromAuth =  SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
//            long userIdFromAuth = (long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            System.out.println("UserController -> username: " + userNameFromAuth);
//            System.out.println("UserController -> userId: " + userIdFromAuth);
//            long userIdFromAuthentication = (long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            System.out.println("login controller --> username : "+userNameFromAuthentication);
//            System.out.println("login controller --> userid : "+userIdFromAuthentication);

            return tokenFactory;
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "incorrect username or password",e);
        }
    }

    @GetMapping("/info")
    public UserDto getCurrentUserInfo(){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.findByUsername(username);
        UserDto userDto = userService.convertToUserDto(user);
        return userDto;
    }

}
