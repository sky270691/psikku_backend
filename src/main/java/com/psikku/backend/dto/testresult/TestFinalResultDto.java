package com.psikku.backend.dto.testresult;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class TestFinalResultDto {

    @JsonProperty("name")
    private String testName;
    @JsonProperty("internal_name")
    private String internalName;
    private LocalDateTime testDateTime;
    private String resultCalculation;
    private String result;
    private boolean hide;

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public LocalDateTime getTestDateTime() {
        return testDateTime;
    }

    public void setTestDateTime(LocalDateTime testDateTime) {
        this.testDateTime = testDateTime;
    }

    public String getResultCalculation() {
        return resultCalculation;
    }

    public void setResultCalculation(String resultCalculation) {
        this.resultCalculation = resultCalculation;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }
}
