package com.risky.jwtresourceserver.service;

import com.risky.jwtresourceserver.entity.TokenFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.DatatypeConverter;
import java.util.Base64;

@Service
public class CustomClientTokenService {
    @Value(value = "${tokensource.host}")
    private String endpoint;

    public TokenFactory getToken(String username, String password){

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

        // assign the response body to tokenfactory object
        TokenFactory tf = responseEntity.getBody();

        return tf;
    }

}
