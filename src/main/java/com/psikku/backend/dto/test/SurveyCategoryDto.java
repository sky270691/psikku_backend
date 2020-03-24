package com.psikku.backend.dto.test;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SurveyCategoryDto {

    @JsonProperty("category_number")
    private int categoryNumber;
    private String category;

    public int getCategoryNumber() {
        return categoryNumber;
    }

    public void setCategoryNumber(int categoryNumber) {
        this.categoryNumber = categoryNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
