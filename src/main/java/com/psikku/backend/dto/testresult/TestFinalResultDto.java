package com.psikku.backend.dto.testresult;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class TestFinalResultDto {

    @JsonProperty("test_name")
    private String testName;
    private LocalDateTime testDateTime;
    private String result;

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public LocalDateTime getTestDateTime() {
        return testDateTime;
    }

    public void setTestDateTime(LocalDateTime testDateTime) {
        this.testDateTime = testDateTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
