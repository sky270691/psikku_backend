package com.psikku.backend.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserResetPasswordDto {

    private String username;

    @NotBlank(message = "password shouldn't be blank")
    @Size(min = 6, message = "password should be at least 6 character")
    @Pattern(regexp = "^(?=.*[a-zA-Z\\d].*)[a-zA-Z\\d!@#$%&*-]{6,}$",message = "password should contain number and alphabet")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserResetPasswordDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
