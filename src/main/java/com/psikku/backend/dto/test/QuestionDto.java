package com.psikku.backend.dto.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"id","question_content","answers"})
public class QuestionDto{

    private String id;

    @JsonProperty(value = "question_content")
    private List<String> questionContent;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String questionCategory;

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

    public String getQuestionCategory() {
        return questionCategory;
    }

    public void setQuestionCategory(String questionCategory) {
        this.questionCategory = questionCategory;
    }

    public List<AnswerDto> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDto> answers) {
        this.answers = answers;
    }

}
