package com.psikku.backend.dto.user;

import com.psikku.backend.validation.MyCustomValidation;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public class UserRegisterDto {

    @NotBlank(message = "username shouldn't be blank")
    private String username;

    @NotBlank(message = "password shouldn't be blank")
    @Size(min = 6, message = "password should be at least 6 character")
    @Pattern(regexp = "\\d+[a-z]+|[a-z]+\\d+",message = "password should contain number and alphabet")
    private String password;
    private String firstname;
    private String lastname;

    @NotBlank(message = "sex shouldn't be blank")
    @Pattern(regexp = "(male)|(female)", message = "should be either `male` or `female`")
    private String sex;

    @NotBlank(message = "email shouldn't be blank")
    @Pattern(regexp = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{1,}$", message = "email format should be valid")
    private String email;
//    private LocalDateTime createTime;

    @Past(message = "so lahir so bro?")
    @MyCustomValidation(message = "blum 17 taong bro")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    private List<RoleRegisterDto> roles;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public List<RoleRegisterDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleRegisterDto> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
