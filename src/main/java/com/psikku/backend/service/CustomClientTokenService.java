package com.psikku.backend.service;

import com.psikku.backend.entity.TokenFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class CustomClientTokenService {
    @Value(value = "${auth-server.tokensource.endpoint}")
    private String endpoint;

    public ResponseEntity<TokenFactory> getToken(String username, String password){

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        // set content type to URLENCODED for request token only support this media type
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String originalString = "psikkuapp:secret";
        String encodedString = Base64.getEncoder().encodeToString(originalString.getBytes());
        headers.set("Authorization", "Basic "+encodedString);

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("scope","write");
        map.add("grant_type","password");
        map.add("username", username);
        map.add("password",password);


//        dataToPost.setScope("write");
//        dataToPost.setGrant_type("password");
//        dataToPost.setPassword("12345");
//        dataToPost.setUsername("john");

        // request
        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(map,headers);

        // response
        ResponseEntity<TokenFactory> responseEntity=  restTemplate.postForEntity(endpoint,entity,TokenFactory.class);


        return responseEntity;
    }

}
