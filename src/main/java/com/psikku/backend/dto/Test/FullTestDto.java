package com.psikku.backend.dto.Test;

import javax.validation.constraints.Pattern;
import java.util.List;

public class FullTestDto {

    private int id;

    @Pattern(regexp = "[^_]+", message = "test name cannot contain '_' character")
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
        this.name = name.toLowerCase();
    }

    public List<SubtestDto> getSubtests() {
        return subtests;
    }

    public void setSubtests(List<SubtestDto> subtests) {
        this.subtests = subtests;
    }
}
