package com.psikku.backend.dto;

import java.util.List;

public class TestDto {

    private String name;
//    @JsonProperty(value = "subtest")
    List<SubtestDto> subtests;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubtestDto> getSubtests() {
        return subtests;
    }

    public void setSubtests(List<SubtestDto> subtests) {
        this.subtests = subtests;
    }
}
