package com.psikku.backend.controller;

import com.psikku.backend.dto.user.UserDto;
import com.psikku.backend.dto.user.UserRegisterDto;
import com.psikku.backend.dto.user.UserRegisterResponse;
import com.psikku.backend.entity.TokenFactory;
import com.psikku.backend.entity.User;
import com.psikku.backend.service.jwttoken.TokenService;
import com.psikku.backend.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    TokenService clientTokenService;

    @Autowired
    UserService userService;


    private TokenFactory tokenFactory;

    @GetMapping
    public List<UserDto> getAllUser(HttpServletRequest request){
        System.out.println(request.getRemoteHost());

        List<User> userList = userService.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for(User user : userList){
            userDtoList.add(userService.convertToUserDto(user));
        }
        return userDtoList;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> register(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        logger.info("new username: '"+userRegisterDto.getUsername()+"' try to register");


//        dummy response
//        UserRegisterResponse response = new UserRegisterResponse();
//        response.setStatus("success");
//        response.setMessage("success");
//        response.setUsername(userRegisterDto.getUsername());
//        response.setId(0);


        return userService.registerNewUserToAuthServer(userRegisterDto);
//        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PutMapping("/register")
    public ResponseEntity<UserRegisterResponse> updateUser(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        logger.info("username: '"+userRegisterDto.getUsername()+"' try to update data");

        return userService.updateUser(userRegisterDto);
    }

//    @PostMapping(value = "/testuser2", consumes = "multipart/form-data")
//    public ResponseEntity<UserRegisterResponse> testUser2(UserRegisterResponse userRegisterResponse){
//        return new ResponseEntity<>(userRegisterResponse,HttpStatus.OK);
//    }

    @PostMapping(value = "/login")
    public TokenFactory login (@RequestPart String username, @RequestPart String password){
        try{
            TokenFactory tokenFactory = userService.loginExistingUser(username.trim(),password);
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

        userDto.setFirstname(firstLetterUpperCase(userDto.getFirstname()));
        userDto.setLastname(firstLetterUpperCase(userDto.getLastname()));
        logger.info("username: '"+username+"' get user info");

        return userDto;
    }

    private String firstLetterUpperCase(String textToCapitalize){
        String[] textSplit = textToCapitalize.split("");
        String result = textSplit[0].toUpperCase()+textToCapitalize.substring(1);
        return result;
    }

}
