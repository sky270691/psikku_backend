package com.risky.jwtresourceserver.service;

import com.risky.jwtresourceserver.entity.User;

public interface UserService {

    public User findUserByUsername(String username);

}
