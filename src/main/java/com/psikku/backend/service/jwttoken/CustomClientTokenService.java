package com.psikku.backend.service.jwttoken;

import com.psikku.backend.entity.TokenFactory;
import com.psikku.backend.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class CustomClientTokenService implements TokenService{
    @Value(value = "${auth-server.endpoint.tokensource}")
    private String endpoint;

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<TokenFactory> getToken(String username, String password) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        // set content type to URLENCODED for request token only support this media type
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String originalString = "psikkuapp:secret";
        String encodedString = Base64.getEncoder().encodeToString(originalString.getBytes());
        headers.set("Authorization", "Basic "+encodedString);

        // request body
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("scope","write");
        map.add("grant_type","password");
        map.add("username", username);
        map.add("password",password);

        // request
        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(map,headers);

        // response
        ResponseEntity<TokenFactory> responseEntity;
        try {
            responseEntity = restTemplate.postForEntity(endpoint,entity, TokenFactory.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new BadCredentialsException(getClass().getSimpleName()+": "+"Error User Name or Password");
        }
//        TokenFactory tf = responseEntity.getBody();
//        ExtractedToken extractedToken = extractToken(tf.getAccess_token());
//        String tokenUsername = extractedToken.getUser_name();
//        User user = userService.findByUsername(tokenUsername);
//        TokenAuthentication tokenAuthentication = new TokenAuthentication(user.getId(),tokenUsername);
//        tokenAuthentication.setUserId(user.getId());
//        SecurityContextHolder.getContext().setAuthentication(tokenAuthentication);
        return responseEntity;
    }



}
