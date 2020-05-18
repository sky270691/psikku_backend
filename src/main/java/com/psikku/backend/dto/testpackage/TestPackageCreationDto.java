package com.psikku.backend.dto.testpackage;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TestPackageCreationDto {

    private String name;

    @JsonProperty("company_id")
    private long companyId;

    @JsonProperty("test_list")
    private List<Integer> testIdList;

    @JsonProperty("user_count")
    private int numOfUser;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public List<Integer> getTestIdList() {
        return testIdList;
    }

    public void setTestIdList(List<Integer> testIdList) {
        this.testIdList = testIdList;
    }

    public int getNumOfUser() {
        return numOfUser;
    }

    public void setNumOfUser(int numOfUser) {
        this.numOfUser = numOfUser;
    }
}
