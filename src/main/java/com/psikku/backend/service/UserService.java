package com.psikku.backend.service;

import com.psikku.backend.dto.user.UserDto;
import com.psikku.backend.dto.user.UserRegisterResponse;
import com.psikku.backend.entity.TokenFactory;
import com.psikku.backend.entity.User;
import com.psikku.backend.dto.user.UserRegisterDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    User findById(long id);
    User findByUsername(String username);
    ResponseEntity<UserRegisterResponse> registerNewUserToAuthServer(UserRegisterDto userRegisterDto);
    TokenFactory loginExistingUser(String username, String password);
    List<User> findAll();
    UserDto convertToUserDto(User user);
    String getUserNameFromToken(String token);
//    public User convertToUserEntity(UserRegisterAuthServerResponse userRegisterAuthServerResponse);
//    public UserRegisterResponse registerResponse(UserRegisterDto userRegisterDto, boolean status);

}
