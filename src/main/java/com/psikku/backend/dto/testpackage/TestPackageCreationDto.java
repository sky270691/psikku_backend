package com.psikku.backend.dto.testpackage;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

public class TestPackageCreationDto {

    private String name;

    private String description;

    @JsonProperty("company_id")
    private long companyId;

    @JsonProperty("test_list")
    private List<@Positive(message = "testidList must be positive value") Integer> testIdList;

    @JsonProperty("user_count")
    @Positive(message = "'user_count' must be a positive value")
    private int numOfUser;

    @PositiveOrZero(message = "'price' can't be a negative value")
    private int price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
