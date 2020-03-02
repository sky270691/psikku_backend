package com.risky.jwtresourceserver.service;

import com.risky.jwtresourceserver.entity.TokenFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class CustomClientTokenService {

    private TokenFactory tokenFactory = new TokenFactory();

    public TokenFactory getToken(){
        String endpoint = "http://localhost:8080/oauth/token";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        // set content type to URLENCODED for request token only support this media type
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String originalString = "client:secret";
        String encodedString = Base64.getEncoder().encodeToString(originalString.getBytes());
        headers.set("Authorization", "Basic "+encodedString);

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("scope","write");
        map.add("grant_type","password");
        map.add("username", "komo");
        map.add("password","admin123");

//        dataToPost.setScope("write");
//        dataToPost.setGrant_type("password");
//        dataToPost.setPassword("12345");
//        dataToPost.setUsername("john");

        // request
        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(map,headers);

        // response
        ResponseEntity<TokenFactory> responseEntity=  restTemplate.postForEntity(endpoint,entity,TokenFactory.class);

        // assign the response body to tokenfactory object
        TokenFactory tf = responseEntity.getBody();

        return tf;
    }

}
