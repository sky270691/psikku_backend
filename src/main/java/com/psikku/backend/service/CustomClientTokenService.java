package com.psikku.backend.service;

import com.psikku.backend.entity.ExtractedToken;
import com.psikku.backend.entity.TokenFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
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
            throw new BadCredentialsException("Error User Name or Password");
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

    @Override
    public ExtractedToken extractToken(String token) {
        Jwt jwt = JwtHelper.decode(token);
        String[] extractedSplitPayload = jwt.getClaims().split(",");

        long exp = Long.parseLong(extractedSplitPayload[0].substring(7,extractedSplitPayload[0].length()-1));
        String username = extractedSplitPayload[1].substring(13,extractedSplitPayload[1].length()-1);
        String authorities = extractedSplitPayload[2].substring(16,extractedSplitPayload[2].length()-2);
        String jti = extractedSplitPayload[3].substring(8,extractedSplitPayload[3].length()-1);
        String client_id = extractedSplitPayload[4].substring(13,extractedSplitPayload[4].length()-1);
        String scope = extractedSplitPayload[5].substring(10,extractedSplitPayload[5].length()-3);

        ExtractedToken extractedToken = new ExtractedToken(exp,username, Arrays.asList(authorities),jti,client_id,Arrays.asList(scope));
        return extractedToken;
    }

}
