package com.psikku.backend.controller;

import com.psikku.backend.dto.user.UserDto;
import com.psikku.backend.dto.user.UserRegisterDto;
import com.psikku.backend.dto.user.UserRegisterResponse;
import com.psikku.backend.entity.TokenFactory;
import com.psikku.backend.entity.User;
import com.psikku.backend.service.CustomClientTokenService;
import com.psikku.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/users")
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



//    @PostMapping(value = "/register2", consumes = "multipart/form-data")
//    public ResponseEntity<UserRegisterResponse> register2(@RequestPart String username,
//                                                          @RequestPart String password,
//                                                          @RequestPart String firstname,
//                                                          @RequestPart String lastname,
//                                                          @RequestPart String sex,
//                                                          @RequestPart String email,
//                                                          @RequestPart String dateOfBirth){
//        UserRegisterDto userRegisterDto = new UserRegisterDto();
//        userRegisterDto.setUsername(username);
//        userRegisterDto.setPassword(password);
//        userRegisterDto.setFirstname(firstname);
//        userRegisterDto.setLastname(lastname);
//        userRegisterDto.setSex(sex);
//        userRegisterDto.setEmail(email);
//        userRegisterDto.setDateOfBirth(LocalDate.parse(dateOfBirth));
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<UserRegisterResponse> test = restTemplate.postForEntity("http://localhost/api/users/register",userRegisterDto,UserRegisterResponse.class);
//
//        return test;
////        return userService.registerNewUserToAuthServer(userRegisterDto);
//
//    }

//    @PostMapping("/testuser2")
//    public String testUser2(@RequestBody UserRegisterResponse userRegisterResponse){
//        return userRegisterResponse.toString();
//    }

    @PostMapping(value = "/testuser2", consumes = "multipart/form-data")
    public ResponseEntity<UserRegisterResponse> testUser2(UserRegisterResponse userRegisterResponse){
        return new ResponseEntity<>(userRegisterResponse,HttpStatus.OK);
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
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "incorrect username or password",e);
        }
    }


//    @PostMapping("/login2")
//    public TokenFactory login2(@RequestPart String header) {
//
//        String[] credential = header.split(" ");
//        byte[] decoded = Base64.getDecoder().decode(credential[1]);
//        String fullCredential = new String(decoded);
//        String[] extractedFullCredential = fullCredential.split(":");
//        String username = extractedFullCredential[0];
//        String password = extractedFullCredential[1];
//        try {
//            TokenFactory tokenFactory = userService.loginExistingUser(username,password);
//            this.tokenFactory = tokenFactory;
//            return tokenFactory;
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "incorrect username or password",e);
//        }
//    }

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
