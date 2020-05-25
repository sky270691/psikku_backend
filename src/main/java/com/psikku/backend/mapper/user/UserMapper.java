package com.psikku.backend.mapper.user;

import com.psikku.backend.dto.user.UserDto;
import com.psikku.backend.dto.user.UserRegisterAuthServerResponse;
import com.psikku.backend.dto.user.UserRegisterResponse;
import com.psikku.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

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
        return userDto;
    }

}
