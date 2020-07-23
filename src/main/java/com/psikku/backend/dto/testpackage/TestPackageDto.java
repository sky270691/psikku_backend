package com.psikku.backend.dto.testpackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.psikku.backend.dto.test.MinimalTestDto;

import java.util.List;

public class TestPackageDto {

    private int id;

    private String name;

    private int price;

    private String description;

    @JsonProperty(value = "view_type")
    private String viewType;

    @JsonProperty("tests")
    private List<MinimalTestDto> minimalTestDtoList;

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<MinimalTestDto> getMinimalTestDtoList() {
        return minimalTestDtoList;
    }

    public void setMinimalTestDtoList(List<MinimalTestDto> minimalTestDtoList) {
        this.minimalTestDtoList = minimalTestDtoList;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
