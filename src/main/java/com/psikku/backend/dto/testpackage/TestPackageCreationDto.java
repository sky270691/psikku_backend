package com.psikku.backend.dto.testpackage;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

public class TestPackageCreationDto {

    private String name;

    private String description;

    @JsonProperty("company_id")
    private long companyId;

    @JsonProperty("test_list")
    private List<Integer> testListId;

    @JsonProperty("user_count")
    @Positive(message = "'user_count' must be a positive value")
    private int numOfUser;

    @PositiveOrZero(message = "'price' can't be a negative value")
    private int price;

    private boolean requiredPreRegister;

    @NotBlank
    @Pattern(regexp = "(BANNER) | (LIST) | (COMBINE)")
    @JsonProperty("view_type")
    private String viewType;

    @NotBlank
    @Pattern(regexp = "(AC) | (SCHOOL) | (PUBLIC) | (INSTANCE) ")
    private String category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getTestListId() {
        return testListId;
    }

    public void setTestListId(List<Integer> testListId) {
        this.testListId = testListId;
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

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isRequiredPreRegister() {
        return requiredPreRegister;
    }

    public void setRequiredPreRegister(boolean requiredPreRegister) {
        this.requiredPreRegister = requiredPreRegister;
    }
}
