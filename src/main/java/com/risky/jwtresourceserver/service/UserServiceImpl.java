package com.risky.jwtresourceserver.service;

import com.risky.jwtresourceserver.dto.UserRegisterDto;
import com.risky.jwtresourceserver.entity.TokenFactory;
import com.risky.jwtresourceserver.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    CustomClientTokenService clientTokenService;

    @Override
    public User findUserByUsername(String username) {
        return null;
    }

    @Override
    public String registerNewUser(UserRegisterDto userRegisterDto) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/new_user",userRegisterDto,String.class);
        return response.toString();
    }

    @Override
    public TokenFactory loginExistingUser(String username, String password) {
        return clientTokenService.getToken(username,password);
    }
}
