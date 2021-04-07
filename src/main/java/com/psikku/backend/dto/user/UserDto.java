package com.psikku.backend.dto.user;

import com.psikku.backend.dto.user.detail.EducationDto;
import com.psikku.backend.dto.user.detail.WorkExperienceDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class UserDto {

    long id;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String sex;
    private String province;
    private String city;
    private String address;
    private String sim;
    private String maritalStatus;
    private String geolocation;
    private LocalDateTime createTime;
    private LocalDate dateOfBirth;
    private List<EducationDto> educationList;
    private List<WorkExperienceDto> workExperienceList;
    private String pictureUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSim() {
        return sim;
    }

    public void setSim(String sim) {
        this.sim = sim;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public List<EducationDto> getEducationList() {
        return educationList;
    }

    public void setEducationList(List<EducationDto> educationList) {
        this.educationList = educationList;
    }

    public List<WorkExperienceDto> getWorkExperienceList() {
        return workExperienceList;
    }

    public void setWorkExperienceList(List<WorkExperienceDto> workExperienceList) {
        this.workExperienceList = workExperienceList;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(String geolocation) {
        this.geolocation = geolocation;
    }
}
