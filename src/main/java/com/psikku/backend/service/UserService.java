package com.psikku.backend.service;

import com.psikku.backend.dto.UserRegisterResponse;
import com.psikku.backend.entity.TokenFactory;
import com.psikku.backend.entity.User;
import com.psikku.backend.dto.UserRegisterDto;
import com.psikku.backend.dto.UserRegisterAuthServerResponse;
import org.springframework.http.ResponseEntity;

import java.sql.SQLIntegrityConstraintViolationException;

public interface UserService {

    public User findUserByUsername(String username);
    public UserRegisterResponse registerNewUserToAuthServer(UserRegisterDto userRegisterDto);
    public TokenFactory loginExistingUser(String username, String password);
//    public User convertToUserEntity(UserRegisterAuthServerResponse userRegisterAuthServerResponse);
//    public UserRegisterResponse registerResponse(UserRegisterDto userRegisterDto, boolean status);

}
