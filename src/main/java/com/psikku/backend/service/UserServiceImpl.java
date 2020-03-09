package com.psikku.backend.service;

import com.psikku.backend.dto.user.*;
import com.psikku.backend.entity.TokenFactory;
import com.psikku.backend.entity.User;
import com.psikku.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    CustomClientTokenService clientTokenService;

    @Autowired
    UserRepository userRepository;

    @Value(value = "${auth-server.enpdoint.users}")
    private String usersEndpoint;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) {
        return null;
    }

    @Override
    public ResponseEntity<UserRegisterResponse> registerNewUserToAuthServer(UserRegisterDto userRegisterDto) {
        RestTemplate restTemplate = new RestTemplate();
        RoleRegisterDto userRole = new RoleRegisterDto();
        userRole.setName("ROLE_USER");
        List<RoleRegisterDto> roleRegisterDtoList = new ArrayList<>();
        roleRegisterDtoList.add(userRole);

        userRegisterDto.setRoles(roleRegisterDtoList);
//        ResponseEntity<String> responseString = restTemplate.postForEntity(usersEndpoint,userRegisterDto, String.class);
        ResponseEntity<UserRegisterAuthServerResponse> responseJson = restTemplate.postForEntity(usersEndpoint,userRegisterDto, UserRegisterAuthServerResponse.class);
        User user =  convertToUserEntity(responseJson.getBody());
        if(user.getId()==0){
            UserRegisterResponse urr = new UserRegisterResponse();
            urr.setUsername(userRegisterDto.getUsername());
            urr.setMessage("Email or password already registered");
            urr.setStatus("Failed");
            return new ResponseEntity<>(urr, HttpStatus.BAD_REQUEST);
        }
        userRepository.save(user);
        UserRegisterResponse userRegisterResponse = convertUserEntityToUserRegisterResponse(user);
        return new ResponseEntity<>(userRegisterResponse, HttpStatus.OK);
    }

    @Override
    public TokenFactory loginExistingUser(String username, String password) {
        TokenFactory tf = clientTokenService.getToken(username,password).getBody();
        tf.setStatus("Success");
        return tf;
    }

    private User convertToUserEntity(UserRegisterAuthServerResponse userRegisterAuthServerResponse) {

        User user = new User();
        user.setUsername(userRegisterAuthServerResponse.getUsername());
        user.setId(userRegisterAuthServerResponse.getId());
        user.setFullName(userRegisterAuthServerResponse.getFullname());
        user.setEmail(userRegisterAuthServerResponse.getEmail());
        user.setCreateTime(userRegisterAuthServerResponse.getCreateTime());
        user.setDateOfBirth(userRegisterAuthServerResponse.getDateOfBirth());

        return user;
    }

    private UserRegisterResponse convertUserEntityToUserRegisterResponse (User user){
        UserRegisterResponse userRegisterResponse = new UserRegisterResponse();
        userRegisterResponse.setId(user.getId());
        userRegisterResponse.setUsername(user.getUsername());
        if(user != null){
            userRegisterResponse.setStatus("success");
            userRegisterResponse.setMessage("User " + user.getUsername() +" has been successfully registered");
        }else{
            userRegisterResponse.setStatus("Failed");
            userRegisterResponse.setMessage("Error Registering: " + user.getUsername());
        }

        return userRegisterResponse;
    }

    @Override
    public UserDto convertToUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setFullName(user.getFullName());
        userDto.setDateOfBirth(user.getDateOfBirth());
        userDto.setCreateTime(user.getCreateTime());
        return userDto;
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
