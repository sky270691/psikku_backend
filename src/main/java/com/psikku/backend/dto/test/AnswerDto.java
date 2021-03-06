package com.psikku.backend.dto.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id","answer_content","is_correct"})
public class AnswerDto {

    private String id;
    @JsonProperty(value = "answer_content")
    private String answerContent;
    @JsonProperty(value = "is_correct")
    private int isCorrect;
    @JsonProperty(value = "answer_category")
    private String answerCategory;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public int getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(int isCorrect) {this.isCorrect = isCorrect;}

    public String getAnswerCategory() {
        return answerCategory;
    }

    public void setAnswerCategory(String answerCategory) {
        this.answerCategory = answerCategory;
    }
}
