package com.psikku.backend.service.user;

import com.psikku.backend.dto.user.*;
import com.psikku.backend.entity.TokenFactory;
import com.psikku.backend.entity.User;
import com.psikku.backend.mapper.user.UserMapper;
import com.psikku.backend.repository.UserRepository;
import com.psikku.backend.service.jwttoken.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TokenService clientTokenService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

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
    public List<UserDto> getAllUserDto() {
        List<User> userList = findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for(User user : userList){
            userDtoList.add(userMapper.convertToUserDto(user));
        }
        return userDtoList;
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
        
        User user =  userMapper.convertRegisteredAuthServerUserToUserEntity(responseJson.getBody());
        if(user.getId()==0){ // if user.getId() from auth server equals to 0 then return error response
            UserRegisterResponse urr = new UserRegisterResponse();
            urr.setUsername(userRegisterDto.getUsername());
            urr.setMessage("Email or username already registered");
            urr.setStatus("Failed");
            return new ResponseEntity<>(urr, HttpStatus.BAD_REQUEST);
        }
        userRepository.save(user);
        UserRegisterResponse userRegisterResponse = userMapper.convertUserEntityToUserRegisterResponse(user);
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
            user.setAddress(userRegisterDto.getAddress());
            user.setCity(userRegisterDto.getCity());
            user.setProvince(userRegisterDto.getProvince());


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

    @Override
    public String getUserNameFromToken(String token){
        Jwt jwt = JwtHelper.decode(token);
        String[] tokenDataSplit = jwt.getClaims().split(",");
        String userNamePair = tokenDataSplit[1];
        String[] userNamePairSplit = userNamePair.split(":");
        String userName = userNamePairSplit[1];
        return userName.substring(1,userName.length()-1);
    }

    @Override
    public UserDto getCurrentUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        logger.info("username: '"+username+"' get user info");
        User user = findByUsername(username);
        UserDto userDto = userMapper.convertToUserDto(user);
        userDto.setFirstname(firstLetterUpperCase(userDto.getFirstname()));
        userDto.setLastname(firstLetterUpperCase(userDto.getLastname()));
        return userDto;
    }

    private String firstLetterUpperCase(String textToCapitalize){
        String[] textSplit = textToCapitalize.split("");
        String result = textSplit[0].toUpperCase()+textToCapitalize.substring(1);
        return result;
    }

}
