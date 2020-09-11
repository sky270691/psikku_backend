package com.psikku.backend.service.user;

import com.psikku.backend.dto.user.*;
import com.psikku.backend.entity.TokenFactory;
import com.psikku.backend.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User findById(long id);
    User findByUsername(String username);
    Optional<User> findByEmail(String email);
    void sendResetPasswordCodeToEmail(String email);
    ResponseEntity<UserRegisterResponse> registerNewUserToAuthServer(UserRegisterDto userRegisterDto);
    TokenFactory loginExistingUser(String username, String password);
    ResponseEntity<UserRegisterResponse> updateUser(UserRegisterDto userRegisterDto,String password);
    void updateCompleteUser(UserUpdateDto userUpdateDto);
    List<User> findAll();
//    UserDto convertToUserDto(User user);
    String getUserNameFromToken(String token);
    UserDto getCurrentUserInfo();
    List<UserDto> getAllUserDto();
    Optional<UserRegisterDto> validateResetPasswordCode(String code);
    ResponseEntity<UserRegisterResponse>updatePassword(UserResetPasswordDto userResetPasswordDto, String code);
//    public User convertToUserEntity(UserRegisterAuthServerResponse userRegisterAuthServerResponse);
//    public UserRegisterResponse register  Response(UserRegisterDto userRegisterDto, boolean status);

}
