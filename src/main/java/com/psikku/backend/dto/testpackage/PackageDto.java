package com.psikku.backend.dto.testpackage;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PackageDto {

    private int id;

    private String name;

    private int price;

    @JsonProperty("tests")
    private List<Integer> testIdList;

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

    public List<Integer> getTestIdList() {
        return testIdList;
    }

    public void setTestIdList(List<Integer> testIdList) {
        this.testIdList = testIdList;
    }
}
