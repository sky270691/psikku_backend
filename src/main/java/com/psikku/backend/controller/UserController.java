package com.psikku.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psikku.backend.dto.user.UserDto;
import com.psikku.backend.dto.user.UserRegisterDto;
import com.psikku.backend.dto.user.UserRegisterResponse;
import com.psikku.backend.entity.TokenFactory;
import com.psikku.backend.service.user.UserService;
import net.bytebuddy.utility.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Valid
public class UserController {

    private final Logger logger;
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        logger = LoggerFactory.getLogger(UserController.class);
    }

    @GetMapping
    public List<UserDto> getAllUser(HttpServletRequest request){
        System.out.println(request.getRemoteHost());
        return userService.getAllUserDto();
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> register(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        logger.info("new username: '"+userRegisterDto.getUsername()+"' try to register");

        return userService.registerNewUserToAuthServer(userRegisterDto);
    }

    @PutMapping("/register")
    public ResponseEntity<UserRegisterResponse> updateUser(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        logger.info("username: '"+userRegisterDto.getUsername()+"' try to update data");

        return userService.updateUser(userRegisterDto);
    }

    @PostMapping(value = "/login")
    public TokenFactory login (@RequestPart String username, @RequestPart String password){
        try{
            TokenFactory tokenFactory = userService.loginExistingUser(username.trim().toLowerCase(),password);
            logger.info("username: '"+username+"' try to login");
            return tokenFactory;
        }catch (RuntimeException e){
//            e.printStackTrace();
            logger.info("username: '"+username+"' login error\nstacktrace: "+e.getMessage());
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
            return tokenFactory;
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "incorrect username or password",e);
        }
    }

    @GetMapping("/info")
    public UserDto getCurrentUserInfo(){
        return userService.getCurrentUserInfo();
    }


    @PostMapping(value = "/reset-password/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,String>> requestResetPasswordCode(@PathVariable @Email(regexp =
            "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{1,}$",
            message = "email format should be valid") String email){
        Map<String,String> returnValue = new HashMap<>();
        userService.sendResetPasswordCodeToEmail(email);
        returnValue.put("status","success");
        return new ResponseEntity<>(returnValue,HttpStatus.OK);
    }

    @GetMapping("/reset-password/{code}")
    public ResponseEntity<?> validateResetCode(@PathVariable String code){
        UserRegisterDto user = userService.validateResetPasswordCode(code);
        if(!(user.getUsername().equalsIgnoreCase("")||user.getUsername() == null)){
            return new ResponseEntity<>(user,HttpStatus.OK);
        }else{
            return new ResponseEntity<>("failed",HttpStatus.BAD_REQUEST);
        }
    }

}
