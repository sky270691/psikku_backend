package com.psikku.backend.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.psikku.backend.dto.user.detail.EducationDto;
import com.psikku.backend.dto.user.detail.WorkExperienceDto;
import com.psikku.backend.validation.AgeValidation;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

public class UserUpdateDto {

    @NotBlank(message = "id pengguna tidak boleh kosong")
    @Pattern(regexp ="\\S+", message = "id pengguna tidak boleh mengandung karakter spasi")
    private String username;

//    @NotBlank(message = "kata sandi tidak boleh kosong")
//    @Size(min = 6, message = "kata sandi harus mengandung minimal 6 karakter ")
//    @Pattern(regexp = "(^(?=\\S*([a-z]|[A-Z])+\\S*[0-9]+\\S*)|^(?=\\S*[0-9]+\\S*([a-z]|[A-Z])+\\S*)).{6,}",message = "kata sandi harus mengandung gabungan angka dan huruf")

    private String password;

    private String newPassword;

    @NotBlank(message = "nama depan tidak boleh kosong")
    private String firstname;

    @NotBlank(message = "nama belakang/marga tidak boleh kosong")
    private String lastname;

    @NotBlank(message = "jenis kelamin tidak boleh kosong")
    @Pattern(regexp = "(male)|(female)", message = "should be either `male` or `female`")
    private String sex;

    @NotBlank(message = "email tidak boleh kosong")
    @Pattern(regexp = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{1,}$", message = "email format should be valid")
    private String email;

    private String address;

    private String city;

    private String province;

    private String sim;

    private String maritalStatus;

    private String geolocation;

//    private LocalDateTime createTime;

    @Past(message = "mohon periksa kembali untuk tahun kelahirannya")
    @AgeValidation(message = "mohon periksa kembali untuk tahun kelahiran")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<RoleRegisterDto> roles;

    private List<EducationDto> educationList;

    private List<WorkExperienceDto> workExperienceList;

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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(String geolocation) {
        this.geolocation = geolocation;
    }
}
