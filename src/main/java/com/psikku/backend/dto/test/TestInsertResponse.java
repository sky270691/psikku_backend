package com.psikku.backend.dto.test;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestInsertResponse {

    @JsonProperty("test_id")
    private int testId;

    @JsonProperty("test_name")
    private String testName;

    @JsonProperty("message")
    private String message;

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}