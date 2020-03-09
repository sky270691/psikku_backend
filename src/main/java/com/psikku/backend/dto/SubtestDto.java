package com.psikku.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SubtestDto {

    private String id;
    private String guide;

    @JsonProperty(value = "test_type")
    private String testType;
    private List<QuestionDto> questions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public List<QuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDto> questions) {
        this.questions = questions;
    }
}
