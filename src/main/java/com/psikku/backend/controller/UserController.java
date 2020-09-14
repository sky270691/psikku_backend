package com.psikku.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psikku.backend.dto.user.*;
import com.psikku.backend.dto.user.detail.EducationDto;
import com.psikku.backend.dto.user.detail.WorkExperienceDto;
import com.psikku.backend.entity.TokenFactory;
import com.psikku.backend.exception.UserExistException;
import com.psikku.backend.service.user.UserService;
import com.psikku.backend.service.user.UserServiceImpl;
import net.bytebuddy.utility.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.*;

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

        return userService.updateUser(userRegisterDto,null);
    }

    @PutMapping("/update-user-data")
    public ResponseEntity<?> updateCompleteUser(@Valid @RequestBody UserUpdateDto userUpdateDto) {
        logger.info("username: '"+userUpdateDto.getUsername()+"' try to update data");
        userService.updateCompleteUser(userUpdateDto);
        Map<String,String> returnBody = new LinkedHashMap<>();
        returnBody.put("status","success");
        return ResponseEntity.ok(returnBody);
    }

    @PostMapping(value = "/login")
    public TokenFactory login (@RequestPart String username, @RequestPart String password){
        try{
            TokenFactory tokenFactory = userService.loginExistingUser(username.trim().toLowerCase(),password);
            logger.info("username: '"+username+"' try to login");
            return tokenFactory;
        }catch (RuntimeException e){
//            e.printStackTrace();
            logger.error("username: '"+username+"' login error\nstacktrace: "+e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username dan/atau password salah",e);
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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username dan/atau password salah",e);
        }
    }

    @GetMapping("/info")
    public UserDto getCurrentUserInfo(){
        return userService.getCurrentUserInfo();
    }


    @PostMapping(value = "/reset-password/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,String>> requestResetPasswordCode(@PathVariable @Email(regexp =
            "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{1,}$",
            message = "email harus menggunakan format yang benar") String email){
        Map<String,String> returnValue = new HashMap<>();
        userService.sendResetPasswordCodeToEmail(email);
        returnValue.put("status","success");
        return new ResponseEntity<>(returnValue,HttpStatus.OK);
    }

    @GetMapping("/reset-password/{code}")
    public ResponseEntity<?> validateResetCode(@PathVariable String code){
        Optional<UserRegisterDto> user = userService.validateResetPasswordCode(code);
        if(user.isPresent()){
            return new ResponseEntity<>(user,HttpStatus.OK);
        }else{
            Map<String,String> returnFailedBody = new HashMap<>();
            returnFailedBody.put("status","failed");
            return new ResponseEntity<>(returnFailedBody,HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody UserResetPasswordDto usernamePassword, @RequestHeader("X-Code") String code){
        logger.info("username: '"+usernamePassword.getUsername()+"' tried to update password");
        ResponseEntity<UserRegisterResponse> response = userService.updatePassword(usernamePassword,code);
        System.out.println(usernamePassword);
        logger.info("success reset password for user:'"+usernamePassword.getUsername()+"'");
        return response;
    }

    @PutMapping("/education")
    public ResponseEntity<?> addOrUpdateEducation(@RequestBody EducationDto dto){
        userService.addEducation(dto);
        Map<String,String> returnBody = new LinkedHashMap<>();
        returnBody.put("status","success");
        return ResponseEntity.ok(returnBody);
    }

    @PutMapping("/work_experience")
    public ResponseEntity<?> addOrUpdateEducation(@RequestBody WorkExperienceDto dto){
        userService.addWorkExp(dto);
        Map<String,String> returnBody = new LinkedHashMap<>();
        returnBody.put("status","success");
        return ResponseEntity.ok(returnBody);
    }


}
