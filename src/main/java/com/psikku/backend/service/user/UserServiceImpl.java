package com.psikku.backend.service.user;

import com.psikku.backend.dto.user.*;
import com.psikku.backend.dto.user.detail.EducationDto;
import com.psikku.backend.dto.user.detail.WorkExperienceDto;
import com.psikku.backend.entity.*;
import com.psikku.backend.exception.EducationException;
import com.psikku.backend.exception.UserExistException;
import com.psikku.backend.exception.WorkExperienceException;
import com.psikku.backend.mapper.user.UserMapper;
import com.psikku.backend.mapper.user.education.EducationMapper;
import com.psikku.backend.mapper.user.workexperience.WorkExperienceMapper;
import com.psikku.backend.repository.EducationRepository;
import com.psikku.backend.repository.UserRepository;
import com.psikku.backend.repository.WorkExperienceRepository;
import com.psikku.backend.service.company.CompanyService;
import com.psikku.backend.service.email.EmailService;
import com.psikku.backend.service.jwttoken.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
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
    private EducationRepository educationRepository;

    @Autowired
    private WorkExperienceMapper workExperienceMapper;

    @Autowired
    private WorkExperienceRepository workExperienceRepository;

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

    @Value("${auth-server.endpoint.verifypassword}")
    private String verifyPassEndpoint;

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
    public void saveOrUpdateUserEntity(User user) {
        userRepository.save(user);
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
        userRegisterDto.setUsername(userRegisterDto.getEmail());
        ResponseEntity<UserRegisterAuthServerResponse> responseJson = restTemplate.postForEntity(usersEndpoint,userRegisterDto, UserRegisterAuthServerResponse.class);

        User user =  userMapper.convertRegisteredAuthServerUserToUserEntity(responseJson.getBody());
        if(user.getId()==0){ // if user.getId() from auth server equals to 0 then return error response
            throw new UserExistException("username dan/atau email sudah pernah didaftarkan");
        }

        //Todo
        // update later for dynamically assign to company
        Company company = companyService.findById(16);
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
            if(userRegisterDto.getLastname() != null) {
                user.setLastname(userRegisterDto.getLastname());
            }
            user.setSex(userRegisterDto.getSex());
            if(userRegisterDto.getDateOfBirth()!=null) {
                user.setDateOfBirth(userRegisterDto.getDateOfBirth());
            }
            if(user.getAddress() != null) {
                user.setAddress(userRegisterDto.getAddress());
            }
            if(user.getCity() != null) {
                user.setCity(userRegisterDto.getCity());
            }
            if(user.getProvince() != null) {
                user.setProvince(userRegisterDto.getProvince());
            }

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

        VerifyPasswordDto verifyPasswordDto = new VerifyPasswordDto();
        verifyPasswordDto.setUsername(userUpdateDto.getUsername());
        verifyPasswordDto.setPassword(userUpdateDto.getPassword());

        try {
            restTemplate.postForEntity(verifyPassEndpoint,verifyPasswordDto,String.class);
        } catch (RestClientException e) {
            logger.error("password did not match");
            throw new UserExistException("password did not match");
        }


        if(searchResponse.getBody() != null && searchResponse.getBody().getUsername().equalsIgnoreCase(userUpdateDto.getUsername())){
            UserRegisterDto dto = userMapper.convertUserUpdateToUserRegisterDto(userUpdateDto);
            restTemplate.put(usersEndpoint,dto);
            user.setFirstname(userUpdateDto.getFirstname());
            if(userUpdateDto.getLastname() != null) {
                user.setLastname(userUpdateDto.getLastname());
            }
            user.setSex(userUpdateDto.getSex());
            if(userUpdateDto.getDateOfBirth() != null) {
                user.setDateOfBirth(userUpdateDto.getDateOfBirth());
            }
            if(userUpdateDto.getAddress() != null) {
                user.setAddress(userUpdateDto.getAddress());
            }
            if(userUpdateDto.getCity() != null) {
                user.setCity(userUpdateDto.getCity());
            }
            if(userUpdateDto.getProvince() != null) {
                user.setProvince(userUpdateDto.getProvince());
            }
            user.setEmail(userUpdateDto.getEmail());
            if(userUpdateDto.getSim()!= null) {
                user.setSim(userUpdateDto.getSim());
            }
            if(userUpdateDto.getMaritalStatus() != null){
                user.setMaritalStatus(userUpdateDto.getMaritalStatus());
            }
            if(userUpdateDto.getGeolocation() != null){
                user.setGeolocation(userUpdateDto.getGeolocation());
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
    public void addEducation(EducationDto dto) {
        String username =  SecurityContextHolder.getContext().getAuthentication().getName();
        User user = findByUsername(username);

        Education education;
        if(dto.getId() != null && dto.getId()!=0){
            education = educationRepository.findById(dto.getId()).orElseThrow(()-> new EducationException("education with id: '"+dto.getId()+"' not found"));
        }else{
            education = new Education();
        }

        if(dto.getEducationLevel()!=null && !dto.getEducationLevel().equalsIgnoreCase("")) {
            education.setEducationLevel(dto.getEducationLevel());
        }
        if(dto.getGraduatedYear() != null && dto.getGraduatedYear() != 0){
            education.setGraduatedYear(dto.getGraduatedYear());
        }
        if(dto.getInstitutionName() != null && !dto.getInstitutionName().equalsIgnoreCase("")){
            education.setInstitutionName(dto.getInstitutionName());
        }
        if(dto.getMajor() != null && !dto.getMajor().equalsIgnoreCase("")){
            education.setMajor(dto.getMajor());
        }

        if(user.getEducationList() == null){
            user.setEducationList(new ArrayList<>());
        }


        Education addedEducation = educationRepository.save(education);
        if(!user.getEducationList().contains(addedEducation)){
            user.getEducationList().add(addedEducation);
        }
        userRepository.save(user);
    }

    @Override
    public void addWorkExp(WorkExperienceDto dto) {
        String username =  SecurityContextHolder.getContext().getAuthentication().getName();
        User user = findByUsername(username);

        WorkExperience workExp;

        if(dto.getId()!=null && dto.getId()!=0){
            workExp = workExperienceRepository.findById(dto.getId()).orElseThrow(()-> new WorkExperienceException("work experience with id '"+dto.getId()+"' not found"));
        }else{
            workExp = new WorkExperience();
        }

        if(dto.getCompanyName() != null && !dto.getCompanyName().equalsIgnoreCase("")){
            workExp.setCompanyName(dto.getCompanyName());
        }
        if(dto.getJobDesc() != null && !dto.getJobDesc().equalsIgnoreCase("")){
            workExp.setJobDesc(dto.getJobDesc());
        }
        if(dto.getStart() != null){
            workExp.setStart(dto.getStart());
        }
        if(dto.getEnd() != null){
            workExp.setEnd(dto.getEnd());
        }
        if(dto.getJabatan() != null){
            workExp.setJabatan(dto.getJabatan());
        }
        if(dto.getNip() != null){
            workExp.setNip(dto.getNip());
        }

        WorkExperience savedWorkExp = workExperienceRepository.save(workExp);

        if(user.getWorkExperienceList() == null){
            user.setWorkExperienceList(new ArrayList<>());
        }

        if(!user.getWorkExperienceList().contains(savedWorkExp)){
            user.getWorkExperienceList().add(savedWorkExp);
        }

        userRepository.save(user);

    }

    @Override
    public void deleteWorkExpById(Long id) {
        workExperienceRepository.deleteById(id);
    }

    @Override
    public void deleteEducationById(Long id) {
        educationRepository.deleteById(id);
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

        Resource resource = resourceLoader.getResource("classpath:static/company/email-company.txt");
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
