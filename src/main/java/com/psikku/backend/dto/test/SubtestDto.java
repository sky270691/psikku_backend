package com.psikku.backend.dto.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.Pattern;
import java.util.List;

@JsonPropertyOrder({"id","guides","test_type","duration","questions"})
public class SubtestDto {

    private String id;
    private List<String> guides;

    @JsonProperty(value = "test_type")
    @Pattern(regexp = "(objective)|(right_or_wrong)|(two_answers)|(three_answers)|(survey)|(user_input_string)|(user_input_number)",
                    message = "test type should be one of these ('objective', 'right_or_wrong', 'two_answers', 'three_answers', 'survey', " +
                                "'user_input_string', 'user_input_number'")
    private String testType;

    private int duration;

    private List<QuestionDto> questions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getGuides() {
        return guides;
    }

    public void setGuides(List<String> guides) {
        this.guides = guides;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<QuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDto> questions) {
        this.questions = questions;
    }
}
