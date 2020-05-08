package com.psikku.backend.dto.useranswer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UserAnswerDto {

    @JsonProperty("answer-list")
    private List<SubmittedAnswerDto> submittedAnswerDtoList;

    @JsonProperty("creation_date_time")
    private String creationDateTime;


    public List<SubmittedAnswerDto> getSubmittedAnswerDtoList() {
        return submittedAnswerDtoList;
    }

    public void setSubmittedAnswerDtoList(List<SubmittedAnswerDto> submittedAnswerDtoList) {
        this.submittedAnswerDtoList = submittedAnswerDtoList;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }
}
