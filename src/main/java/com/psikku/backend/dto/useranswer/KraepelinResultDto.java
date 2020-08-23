package com.psikku.backend.dto.useranswer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class KraepelinResultDto {

    @JsonProperty("voucher_id")
    private Long voucherId;

    private String result;

    @JsonProperty("creation_date_time")
    private LocalDateTime creationDateTime;


    public Long getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Long voucherId) {
        this.voucherId = voucherId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }
}
