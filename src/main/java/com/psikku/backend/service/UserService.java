package com.psikku.backend.service;

import com.psikku.backend.dto.UserDto;
import com.psikku.backend.dto.UserRegisterResponse;
import com.psikku.backend.entity.TokenFactory;
import com.psikku.backend.entity.User;
import com.psikku.backend.dto.UserRegisterDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    User findUserByUsername(String username);
    ResponseEntity<UserRegisterResponse> registerNewUserToAuthServer(UserRegisterDto userRegisterDto);
    TokenFactory loginExistingUser(String username, String password);
    List<User> findAll();
    UserDto convertToUserDto(User user);
//    public User convertToUserEntity(UserRegisterAuthServerResponse userRegisterAuthServerResponse);
//    public UserRegisterResponse registerResponse(UserRegisterDto userRegisterDto, boolean status);

}
