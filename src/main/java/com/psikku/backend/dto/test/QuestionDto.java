package com.psikku.backend.dto.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"id","question_content","answers"})
public class QuestionDto{

    private String id;

    @JsonProperty(value = "question_content")
    private List<String> questionContent;

    private List<AnswerDto> answers;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(List<String> questionContent) {
        this.questionContent = questionContent;
    }

    public List<AnswerDto> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDto> answers) {
        this.answers = answers;
    }

}
