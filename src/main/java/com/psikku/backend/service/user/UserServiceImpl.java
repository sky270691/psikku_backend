package com.psikku.backend.service.user;

import com.psikku.backend.dto.user.*;
import com.psikku.backend.dto.user.education.EducationDto;
import com.psikku.backend.entity.Company;
import com.psikku.backend.entity.TokenFactory;
import com.psikku.backend.entity.User;
import com.psikku.backend.exception.UserExistException;
import com.psikku.backend.mapper.user.UserMapper;
import com.psikku.backend.mapper.user.education.EducationMapper;
import com.psikku.backend.repository.UserRepository;
import com.psikku.backend.service.company.CompanyService;
import com.psikku.backend.service.email.EmailService;
import com.psikku.backend.service.jwttoken.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final Map<String, User> userTempCodePair = new HashMap<>();

    @Autowired
    private TokenService clientTokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EducationMapper educationMapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ResourceLoader resourceLoader;

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
    public Optional<UserRegisterDto> validateResetPasswordCode(String code) {

        for (Map.Entry<String, User> stringUserEntry : userTempCodePair.entrySet()) {
            if(code.equalsIgnoreCase(stringUserEntry.getKey())){
                User user = userTempCodePair.get(code);
                return Optional.of(userMapper.convertToUserRegisterDto(user));
            }
        }
        return Optional.empty();
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
        if (userTempCodePair.containsValue(user)) {
            for (Map.Entry<String, User> stringUserEntry : userTempCodePair.entrySet()) {
                if(stringUserEntry.getValue().equals(user)){
                    userTempCodePair.remove(stringUserEntry.getKey());
                }
            }
        }
        String uniqueCode = generateAlphaNumeric(6);
        StringBuilder emailMessage = new StringBuilder();
        emailMessage.append("Halo "+user.getFirstname()+" "+user.getLastname()+", silahkan input kode ini di aplikasi Psikku Online Assessment:\n");
        userTempCodePair.put(uniqueCode,user);
        emailMessage.append(uniqueCode);
        emailMessage.append("\n\n\n");
        emailMessage.append("--- Psikku Indonesia ---");
        emailService.sendEmail(email,"Reset Password Psikku User",emailMessage.toString());
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
            throw new UserExistException("username dan/atau email sudah pernah didaftarkan");
        }
        Company company = companyService.findById(15);
        if(checkCompany(userRegisterDto.getEmail())){
            user.setCompany(company);
        }
        userRepository.save(user);
        UserRegisterResponse userRegisterResponse = userMapper.convertUserEntityToUserRegisterResponse(user);
        return new ResponseEntity<>(userRegisterResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserRegisterResponse> updateUser(UserRegisterDto userRegisterDto,String password){
        User user = userRepository.findUserByUsername(userRegisterDto.getUsername());
        System.out.println(password);
        if(password!=null){
            userRegisterDto.setPassword(password);
        }
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
            user.setEmail(userRegisterDto.getEmail());

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
    public void updateCompleteUser(UserUpdateDto userUpdateDto){
        User user = userRepository.findUserByUsername(userUpdateDto.getUsername());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserRegisterResponse> searchResponse = restTemplate.getForEntity(userSearchEndpoint,UserRegisterResponse.class,userUpdateDto.getUsername());

        if(searchResponse.getBody() != null && searchResponse.getBody().getUsername().equalsIgnoreCase(userUpdateDto.getUsername())){
            UserRegisterDto dto = userMapper.convertUserUpdateToUserRegisterDto(userUpdateDto);
            restTemplate.put(usersEndpoint,dto);
            user.setFirstname(userUpdateDto.getFirstname());
            user.setLastname(userUpdateDto.getLastname());
            user.setSex(userUpdateDto.getSex());
            user.setDateOfBirth(userUpdateDto.getDateOfBirth());
            user.setAddress(userUpdateDto.getAddress());
            user.setCity(userUpdateDto.getCity());
            user.setProvince(userUpdateDto.getProvince());
            user.setEmail(userUpdateDto.getEmail());
            user.setSim(userUpdateDto.getSim());
            user.setMaritalStatus(userUpdateDto.getMaritalStatus());

            if(userUpdateDto.getEducationList() != null && !userUpdateDto.getEducationList().isEmpty()){
                user.setEducationList(new ArrayList<>());
                for (EducationDto educationDto : userUpdateDto.getEducationList()) {
                    user.getEducationList().add(educationMapper.convertDtoToEducationEntity(educationDto));
                }
            }

        }
        user.setId(user.getId());
        userRepository.save(user);
    }

    @Override
    public ResponseEntity<UserRegisterResponse> updatePassword(UserResetPasswordDto userResetPasswordDto, String codeHeader) {
        if(codeHeader!=null && userTempCodePair.containsKey(codeHeader)){
            User user;
            try {
                user = findByUsername(userResetPasswordDto.getUsername());
            } catch (NullPointerException e) {
                throw new UserExistException("User is not exist");
            }
            UserRegisterDto userRegisterDto = userMapper.convertToUserRegisterDto(user);
            return updateUser(userRegisterDto,userResetPasswordDto.getPassword());
        }else{
            throw new UserExistException("Error Occured");
        }
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

    private boolean checkCompany(String email){

        Resource resource = resourceLoader.getResource("classpath:static/company/email-bsg.txt");
        try {
            List<String> emailList = Files.readAllLines(Paths.get(resource.getURI()));
            for (String emailListData : emailList) {
                if(email.equalsIgnoreCase(emailListData)){
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    //delete the temp code on 23:59 everyday
    @Scheduled(cron = "0 59 23 * * ?")
    protected void deleteTempCode(){
        if(!userTempCodePair.entrySet().isEmpty()){
            userTempCodePair.clear();
        }
    }

}
