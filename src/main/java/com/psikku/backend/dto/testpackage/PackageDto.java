package com.psikku.backend.dto.testpackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.psikku.backend.dto.test.MinimalTestDto;

import java.util.List;

public class PackageDto {

    private int id;

    private String name;

    private int price;

    @JsonProperty("test_list")
    private List<MinimalTestDto> testList;

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

    public List<MinimalTestDto> getTestList() {
        return testList;
    }

    public void setTestList(List<MinimalTestDto> testList) {
        this.testList = testList;
    }
}
