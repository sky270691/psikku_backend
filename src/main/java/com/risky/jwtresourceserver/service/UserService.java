package com.risky.jwtresourceserver.service;

import com.risky.jwtresourceserver.dto.UserRegisterDto;
import com.risky.jwtresourceserver.entity.TokenFactory;
import com.risky.jwtresourceserver.entity.User;
import org.springframework.http.HttpEntity;

public interface UserService {

    public User findUserByUsername(String username);
    public String registerNewUser(UserRegisterDto userRegisterDto);
    public TokenFactory loginExistingUser(String username, String password);

}
