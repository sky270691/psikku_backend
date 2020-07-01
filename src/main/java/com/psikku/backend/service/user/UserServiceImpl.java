package com.psikku.backend.service.user;

import com.psikku.backend.dto.user.*;
import com.psikku.backend.entity.TokenFactory;
import com.psikku.backend.entity.User;
import com.psikku.backend.exception.UserExistException;
import com.psikku.backend.mapper.user.UserMapper;
import com.psikku.backend.repository.UserRepository;
import com.psikku.backend.service.email.EmailService;
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
import java.security.SecureRandom;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final Map<String, User> userTempCodePair = new HashMap<>();

    @Autowired
    private TokenService clientTokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserMapper userMapper;

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
    public UserRegisterDto validateResetPasswordCode(String code) {

        for (Map.Entry<String, User> stringUserEntry : userTempCodePair.entrySet()) {
            if(code.equalsIgnoreCase(stringUserEntry.getKey())){
                userTempCodePair.remove(code);
                User user = userTempCodePair.get(code);
                return userMapper.convertToUserRegisterDto(user);
            }
        }
        return null;
    }

    @Override
    @Transactional
    public User findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void sendResetPasswordCodeToEmail(String email) {
        User user = findByEmail(email).orElseThrow(()->new UserExistException("User not exist"));
        String uniqueCode = generateAlphaNumeric(6);
        userTempCodePair.put(uniqueCode,user);
        emailService.sendEmail(email,"Reset Password Psikku User",uniqueCode);
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
            throw new UserExistException("Username / email already exist");
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

    private String generateAlphaNumeric(int stringLength){
        String alphaLower = "abcdefghijklmnopqrstuvwxyz";
//        String alphaUpper = alphaLower.toUpperCase();
        String number = "0123456789";

        String combination = alphaLower + number;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stringLength; i++) {
            Random random = new SecureRandom();
            sb.append(combination.charAt(random.nextInt(combination.length())));
        }
        return sb.toString();
    }

}
