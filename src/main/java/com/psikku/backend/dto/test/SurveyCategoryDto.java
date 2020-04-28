package com.psikku.backend.dto.test;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SurveyCategoryDto {

    @JsonProperty("category_number")
    private String categoryNumber;
    private String category;

    public String getCategoryNumber() {
        return categoryNumber;
    }

    public void setCategoryNumber(String categoryNumber) {
        this.categoryNumber = categoryNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
