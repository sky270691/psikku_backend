package com.psikku.backend.dto.useranswer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RawAnswerDto {

    private String voucher;

    private String result;

    @JsonProperty("creation_date_time")
    private String creationDateTime;

    @JsonProperty("test_internal_name")
    private String testInternalName;

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getTestInternalName() {
        return testInternalName;
    }

    public void setTestInternalName(String testInternalName) {
        this.testInternalName = testInternalName;
    }

    @Override
    public String toString() {
        return "RawAnswerDto{" +
                "voucher='" + voucher + '\'' +
                ", result='" + result + '\'' +
                ", creationDateTime='" + creationDateTime + '\'' +
                ", testInternalName='" + testInternalName + '\'' +
                '}';
    }
}
