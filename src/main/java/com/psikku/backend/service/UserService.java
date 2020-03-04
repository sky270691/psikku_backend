package com.psikku.backend.service;

import com.psikku.backend.entity.TokenFactory;
import com.psikku.backend.entity.User;
import com.psikku.backend.dto.UserRegisterDto;
import com.psikku.backend.dto.UserRegisterResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {

    public User findUserByUsername(String username);
    public ResponseEntity<UserRegisterResponse> registerNewUser(UserRegisterDto userRegisterDto);
    public TokenFactory loginExistingUser(String username, String password);
//    public UserRegisterResponse registerResponse(UserRegisterDto userRegisterDto, boolean status);

}
