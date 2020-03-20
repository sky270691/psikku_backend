package com.psikku.backend.dto.test;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubmittedAnswerDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("answer-1")
    private String answer1;

    @JsonProperty("answer-2")
    private String answer2;

    @JsonProperty("answer-3")
    private String answer3;

    @JsonProperty("user-id")
    private long userId;

    @JsonProperty("question-id")
    private String questionId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
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
