package com.psikku.backend.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.psikku.backend.validation.AgeValidation;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public class UserRegisterDto {

    @NotBlank(message = "username shouldn't be blank")
    @Pattern(regexp ="\\S+", message = "username shouldn't contain any whitespace character")
    private String username;

    @NotBlank(message = "password shouldn't be blank")
    @Size(min = 6, message = "password should be at least 6 character")
    @Pattern(regexp = "^(?=.*[a-zA-Z\\d].*)[a-zA-Z\\d!@#$%&*]{6,}$",message = "password should contain number and alphabet")
    private String password;

    @NotBlank(message = "firstname shouldn't be blank")
    private String firstname;

    @NotBlank(message = "lastname shouldn't be blank")
    private String lastname;

    @NotBlank(message = "sex shouldn't be blank")
    @Pattern(regexp = "(male)|(female)", message = "should be either `male` or `female`")
    private String sex;

    @NotBlank(message = "email shouldn't be blank")
    @Pattern(regexp = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{1,}$", message = "email format should be valid")
    private String email;

    private String address;

    private String city;

    private String province;

//    private LocalDateTime createTime;

    @Past(message = "so lahir so bro?")
//    @AgeValidation(message = "blum 17 taong bro")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
