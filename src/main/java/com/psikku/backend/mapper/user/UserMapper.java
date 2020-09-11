package com.psikku.backend.mapper.user;

import com.psikku.backend.dto.user.*;
import com.psikku.backend.entity.Education;
import com.psikku.backend.entity.User;
import com.psikku.backend.mapper.user.education.EducationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserMapper {

    private final EducationMapper educationMapper;

    @Autowired
    public UserMapper(EducationMapper educationMapper) {
        this.educationMapper = educationMapper;
    }

    public User convertRegisteredAuthServerUserToUserEntity(UserRegisterAuthServerResponse userRegisterAuthServerResponse) {

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

    public UserRegisterResponse convertUserEntityToUserRegisterResponse (User user){
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
        userDto.setProvince(user.getProvince());
        userDto.setCity(user.getCity());
        userDto.setAddress(user.getAddress());
        if(user.getMaritalStatus() != null && !user.getMaritalStatus().equalsIgnoreCase("")) {
            userDto.setMaritalStatus(user.getMaritalStatus());
        }
        if(user.getSim() != null && !user.getSim().equalsIgnoreCase("")) {
            userDto.setSim(user.getSim());
        }
        if(user.getEducationList() != null && !user.getEducationList().isEmpty()){
            userDto.setEducationList(new ArrayList<>());
            for (Education education : user.getEducationList()) {
                userDto.getEducationList().add(educationMapper.convertEntityToEducationDto(education));
            }
        }
        return userDto;
    }

    public UserRegisterDto convertToUserRegisterDto(User user){
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setUsername(user.getUsername());
        userRegisterDto.setSex(user.getSex());
        userRegisterDto.setProvince(user.getProvince());
        userRegisterDto.setFirstname(user.getFirstname());
        userRegisterDto.setLastname(user.getLastname());
        userRegisterDto.setEmail(user.getEmail());
        userRegisterDto.setDateOfBirth(user.getDateOfBirth());
        userRegisterDto.setCity(user.getCity());
        userRegisterDto.setAddress(user.getAddress());
        return userRegisterDto;
    }


//    public User convertUserUpdateDtoToUserEntity(UserUpdateDto dto){
//
//        User user = new User();
//        user.setUsername(dto.getUsername());
//        user.setFirstname(dto.getFirstname());
//        user.setLastname(dto.getLastname());
//        user.setSex(dto.getSex());
//        user.setEmail(dto.getEmail());
//        user.setDateOfBirth(dto.getDateOfBirth());
//
//
//    }

    public UserRegisterDto convertUserUpdateToUserRegisterDto(UserUpdateDto userUpdateDto){
        UserRegisterDto userRegisterDto = new UserRegisterDto();

        userRegisterDto.setUsername(userUpdateDto.getUsername());
        userRegisterDto.setPassword(userUpdateDto.getPassword());
        userRegisterDto.setFirstname(userUpdateDto.getFirstname());
        if(userUpdateDto.getLastname() != null && !userUpdateDto.getLastname().equalsIgnoreCase("")) {
            userRegisterDto.setLastname(userUpdateDto.getLastname());
        }
        userRegisterDto.setSex(userUpdateDto.getSex());
        userRegisterDto.setDateOfBirth(userUpdateDto.getDateOfBirth());
        if(userUpdateDto.getAddress() != null && !userUpdateDto.getAddress().equalsIgnoreCase("")) {
            userRegisterDto.setAddress(userUpdateDto.getAddress());
        }
        if(userUpdateDto.getCity() != null && !userUpdateDto.getCity().equalsIgnoreCase("")) {
            userRegisterDto.setCity(userUpdateDto.getCity());
        }
        if(userUpdateDto.getProvince() != null && !userUpdateDto.getProvince().equalsIgnoreCase("")) {
            userRegisterDto.setProvince(userUpdateDto.getProvince());
        }
        userRegisterDto.setEmail(userUpdateDto.getEmail());
        return  userRegisterDto;
    }

}
