package com.psikku.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class QuestionDto {

    private String id;

    @JsonProperty(value = "question_content")
    private String questionContent;

    private List<AnswerDto> answers;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public List<AnswerDto> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDto> answers) {
        this.answers = answers;
    }

}
