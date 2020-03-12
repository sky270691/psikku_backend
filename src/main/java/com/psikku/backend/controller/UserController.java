package com.psikku.backend.controller;

import com.psikku.backend.dto.user.UserDto;
import com.psikku.backend.dto.user.UserRegisterDto;
import com.psikku.backend.dto.user.UserRegisterResponse;
import com.psikku.backend.entity.TokenFactory;
import com.psikku.backend.entity.User;
import com.psikku.backend.service.CustomClientTokenService;
import com.psikku.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    CustomClientTokenService clientTokenService;

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

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> register(@RequestBody UserRegisterDto userRegisterDto) {
        return userService.registerNewUserToAuthServer(userRegisterDto);
    }


    @PostMapping("/login")
    public TokenFactory login(@RequestHeader("Authorization") String header) {

        String[] credential = header.split(" ");
        byte[] decoded = Base64.getDecoder().decode(credential[1]);
        String fullCredential = new String(decoded);
        String[] extractedFullCredential = fullCredential.split(":");
        String username = extractedFullCredential[0];
        String password = extractedFullCredential[1];
        try {
            TokenFactory tokenFactory = userService.loginExistingUser(username,password);
            this.tokenFactory = tokenFactory;
            return tokenFactory;
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "incorrect username or password",e);
        }
    }

    @GetMapping("/info")
    public UserDto getCurrentUserInfo(@RequestHeader("Authorization") String auth){
        String[] authHeader = auth.split(" ");
        String token = authHeader[1];
        String username = userService.getUserNameFromToken(token);
        User user = userService.findUserByUsername(username);
        UserDto userDto = userService.convertToUserDto(user);
        return userDto;
    }

    //todo
    // tesasdjagsdljgkljasdlfjkljasdlgj
    // jalskdgjlkasjdflkjasljglasjdf
    // jadlkgjasldfjlsadjfl

}
