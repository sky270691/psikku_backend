package com.psikku.backend.dto.useranswer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PauliResultDto {

    @JsonProperty("voucher")
    private String voucher;

    private String result;

    @JsonProperty("creation_date_time")
    private String creationDateTime;

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
}
