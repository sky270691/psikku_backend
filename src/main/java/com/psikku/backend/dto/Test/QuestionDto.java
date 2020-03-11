package com.psikku.backend.dto.Test;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class QuestionDto {

    private String id;

    @JsonProperty(value = "question_content_1")
    private String questionContent1;

    @JsonProperty(value = "question_content_2")
    private String questionContent2;

    @JsonProperty(value = "question_content_3")
    private String questionContent3;

    private List<AnswerDto> answers;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getQuestionContent1() {
        return questionContent1;
    }

    public void setQuestionContent1(String questionContent1) {
        this.questionContent1 = questionContent1;
    }

    public String getQuestionContent2() {
        return questionContent2;
    }

    public void setQuestionContent2(String questionContent2) {
        this.questionContent2 = questionContent2;
    }

    public String getQuestionContent3() {
        return questionContent3;
    }

    public void setQuestionContent3(String questionContent3) {
        this.questionContent3 = questionContent3;
    }

    public List<AnswerDto> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDto> answers) {
        this.answers = answers;
    }

}
