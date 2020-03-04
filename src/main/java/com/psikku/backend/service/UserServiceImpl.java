package com.psikku.backend.service;

import com.psikku.backend.dto.RoleRegisterDto;
import com.psikku.backend.dto.UserRegisterDto;
import com.psikku.backend.dto.UserRegisterResponse;
import com.psikku.backend.entity.TokenFactory;
import com.psikku.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    CustomClientTokenService clientTokenService;

    @Value(value = "${users.enpdoint}")
    private String usersEndpoint;

    @Override
    public User findUserByUsername(String username) {
        return null;
    }

    @Override
    public ResponseEntity<UserRegisterResponse> registerNewUser(UserRegisterDto userRegisterDto) {
        RestTemplate restTemplate = new RestTemplate();
        RoleRegisterDto userRole = new RoleRegisterDto();
        userRole.setName("ROLE_USER");
        List<RoleRegisterDto> roleRegisterDtoList = new ArrayList<>();
        roleRegisterDtoList.add(userRole);

        userRegisterDto.setRoles(roleRegisterDtoList);
        ResponseEntity<UserRegisterResponse> response = restTemplate.postForEntity(usersEndpoint,userRegisterDto,UserRegisterResponse.class);
        return response;
    }

    @Override
    public TokenFactory loginExistingUser(String username, String password) {

        return clientTokenService.getToken(username,password).getBody();
    }

//    @Override
//    public UserRegisterResponse registerResponse(UserRegisterDto userRegisterDto, boolean status) {
//        UserRegisterResponse response = new UserRegisterResponse();
//        response.setUserdataRegisterUsername(userRegisterDto.getUsername());
//        if(status){
//            long id = findUserdataByUsername(userRegisterDto.getUsername()).getId();
//            response.setId(id);
//            response.setMessage("User: " + userRegisterDto.getUsername() + " successfully registered");
//            response.setStatus("success");
//        }else{
//            response.setMessage("User: " + userRegisterDto.getUsername() + " failed to register");
//            response.setStatus("failed");
//        }
//        return response;
//    }
}
