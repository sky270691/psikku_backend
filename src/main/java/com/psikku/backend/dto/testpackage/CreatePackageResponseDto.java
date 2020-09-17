package com.psikku.backend.dto.testpackage;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class CreatePackageResponseDto {
    private int id;
    private String name;
    private int price;
    private String category;
    private String status;

    @JsonProperty("list_of_test")
    private List<String> testList = new ArrayList<>();

    public CreatePackageResponseDto(int id, String name, String status,int price,List<String> testList, String category) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.price = price;
        this.category = category;
        this.testList.addAll(testList);
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<String> getTestList() {
        return testList;
    }

    public void setTestList(List<String> testList) {
        this.testList = testList;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
