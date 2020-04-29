package com.psikku.backend.dto.testresult;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestFinalResultDto {

    private String testDescription;
    private LocalDateTime testDateTime;
    private String result;

    public String getTestDescription() {
        return testDescription;
    }

    public void setTestDescription(String testDescription) {
        this.testDescription = testDescription;
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
