package com.psikku.backend.dto;

import java.util.List;

public class UserRegisterDto {

    private String username;
    private String password;
    private String fullname;
    private List<RoleRegisterDto> roles;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
