package com.psikku.backend.dto.test;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SubmittedAnswerDto {

    @JsonProperty("id")
    private long id;

    @JsonProperty("answers")
    private List<String> answers;

    @JsonProperty("user-id")
    private long userId;

    @JsonProperty("question-id")
    private String questionId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
