package com.psikku.backend.dto.testpackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.psikku.backend.dto.test.MinimalTestDto;

import java.util.List;

public class PackageWithOrderDto{

    @JsonProperty("test_list")
    private List<MinimalTestDto> testList;

    @JsonProperty("current_stop_position")
    private Integer currentStopPosition;

    private boolean rest;

    public Integer getCurrentStopPosition() {
        return currentStopPosition;
    }

    public void setCurrentStopPosition(Integer currentStopPosition) {
        this.currentStopPosition = currentStopPosition;
    }

    public List<MinimalTestDto> getTestList() {
        return testList;
    }

    public void setTestList(List<MinimalTestDto> testList) {
        this.testList = testList;
    }

    public boolean isRest() {
        return rest;
    }

    public void setRest(boolean rest) {
        this.rest = rest;
    }
}
