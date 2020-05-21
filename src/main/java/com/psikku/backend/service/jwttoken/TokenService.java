package com.psikku.backend.service.jwttoken;

import com.psikku.backend.entity.TokenFactory;
import org.springframework.http.ResponseEntity;

public interface TokenService {

    ResponseEntity<TokenFactory> getToken(String username, String password);
}
