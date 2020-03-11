package com.psikku.backend.dto.Test;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnswerDto {

    private String id;
    @JsonProperty(value = "answer_content")
    private String answerContent;
    @JsonProperty(value = "is_correct")
    private int isCorrect;

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

    public void setIsCorrect(int isCorrect) {
        this.isCorrect = isCorrect;
    }
}
