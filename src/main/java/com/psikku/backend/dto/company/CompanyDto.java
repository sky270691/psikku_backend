package com.psikku.backend.dto.company;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompanyDto {

    private int id;

    private String name;

    private String address;

    private String city;

    private String province;

    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    private String email;

    @JsonProperty(value = "display_result")
    private boolean displayResult;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isDisplayResult() {
        return displayResult;
    }

    public void setDisplayResult(boolean displayResult) {
        this.displayResult = displayResult;
    }
}
