package com.psikku.backend.service;

import com.psikku.backend.dto.user.*;
import com.psikku.backend.entity.TokenFactory;
import com.psikku.backend.entity.User;
import com.psikku.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    TokenService clientTokenService;

    @Autowired
    UserRepository userRepository;

    @Value(value = "${auth-server.endpoint.users}")
    private String usersEndpoint;

    @Value("${auth-server.endpoint.searchuser}")
    private String userSearchEndpoint;

    @Override
    public List<User> findAll() {
//        return userRepository.findAll(Sort.by("email").ascending());

        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    @Transactional
    public User findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public ResponseEntity<UserRegisterResponse> registerNewUserToAuthServer(UserRegisterDto userRegisterDto) {
        RestTemplate restTemplate = new RestTemplate();
        RoleRegisterDto userRole = new RoleRegisterDto();
        userRole.setName("ROLE_USER");
        List<RoleRegisterDto> roleRegisterDtoList = new ArrayList<>();
        roleRegisterDtoList.add(userRole);

        userRegisterDto.setRoles(roleRegisterDtoList);
        ResponseEntity<UserRegisterAuthServerResponse> responseJson = restTemplate.postForEntity(usersEndpoint,userRegisterDto, UserRegisterAuthServerResponse.class);
        
        User user =  convertToUserEntity(responseJson.getBody());
        if(user.getId()==0){ // if user.getId() from auth server equals to 0 then return error response
            UserRegisterResponse urr = new UserRegisterResponse();
            urr.setUsername(userRegisterDto.getUsername());
            urr.setMessage("Email or username already registered");
            urr.setStatus("Failed");
            return new ResponseEntity<>(urr, HttpStatus.BAD_REQUEST);
        }
        userRepository.save(user);
        UserRegisterResponse userRegisterResponse = convertUserEntityToUserRegisterResponse(user);
        return new ResponseEntity<>(userRegisterResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserRegisterResponse> updateUser(UserRegisterDto userRegisterDto){
        User user = userRepository.findUserByUsername(userRegisterDto.getUsername());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserRegisterResponse> searchResponse = restTemplate.getForEntity(userSearchEndpoint,UserRegisterResponse.class,userRegisterDto.getUsername());

        if(searchResponse.getBody() != null && searchResponse.getBody().getUsername().equalsIgnoreCase(userRegisterDto.getUsername())){
            restTemplate.put(usersEndpoint,userRegisterDto);
            user.setFirstname(userRegisterDto.getFirstname());
            user.setLastname(userRegisterDto.getLastname());
            user.setSex(userRegisterDto.getSex());
            user.setDateOfBirth(userRegisterDto.getDateOfBirth());

            UserRegisterResponse response = new UserRegisterResponse();
            response.setStatus("success");
            response.setMessage("userdata for username " +userRegisterDto.getUsername()+" updated successfully");
            response.setUsername(userRegisterDto.getUsername());
            response.setId(user.getId());
            userRepository.save(user);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        System.out.println("Error update user");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    @Transactional
    public TokenFactory loginExistingUser(String username, String password) {
        TokenFactory tf = clientTokenService.getToken(username,password).getBody();
        tf.setStatus("Success");
//        Authentication
        return tf;
    }

    private User convertToUserEntity(UserRegisterAuthServerResponse userRegisterAuthServerResponse) {

        User user = new User();
        user.setUsername(userRegisterAuthServerResponse.getUsername());
        user.setId(userRegisterAuthServerResponse.getId());
        user.setFirstname(userRegisterAuthServerResponse.getFirstname());
        user.setLastname(userRegisterAuthServerResponse.getLastname());
        user.setSex(userRegisterAuthServerResponse.getSex());
        user.setEmail(userRegisterAuthServerResponse.getEmail());
        user.setCreateTime(userRegisterAuthServerResponse.getCreateTime());
        user.setModifiedTime(userRegisterAuthServerResponse.getModifiedTime());
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
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setSex(user.getSex());
        userDto.setDateOfBirth(user.getDateOfBirth());
        userDto.setCreateTime(user.getCreateTime());
        return userDto;
    }

    @Override
    public String getUserNameFromToken(String token){
        Jwt jwt = JwtHelper.decode(token);
        String[] tokenDataSplit = jwt.getClaims().split(",");
        String userNamePair = tokenDataSplit[1];
        String[] userNamePairSplit = userNamePair.split(":");
        String userName = userNamePairSplit[1];
        return userName.substring(1,userName.length()-1);
    }

}
