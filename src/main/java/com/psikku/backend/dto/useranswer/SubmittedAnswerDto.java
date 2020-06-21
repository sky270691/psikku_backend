package com.psikku.backend.dto.useranswer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SubmittedAnswerDto {

    private long id;

    @JsonProperty("question_id")
    private String questionId;

    @JsonProperty("answers")
    private List<String> answers;

    private long userId;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SubmittedAnswerDto{" +
                "id=" + id +
                ", questionId='" + questionId + '\'' +
                ", answers=" + answers +
                ", userId=" + userId +
                '}';
    }
}
