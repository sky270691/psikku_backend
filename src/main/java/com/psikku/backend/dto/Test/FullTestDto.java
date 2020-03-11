package com.psikku.backend.dto.Test;

import java.util.List;

public class FullTestDto {

    private int id;

    private String name;

//    @JsonProperty(value = "subtest")

    List<SubtestDto> subtests;

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

    public List<SubtestDto> getSubtests() {
        return subtests;
    }

    public void setSubtests(List<SubtestDto> subtests) {
        this.subtests = subtests;
    }
}
