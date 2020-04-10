package com.psikku.backend.dto.testresult;

import com.fasterxml.jackson.annotation.*;

import java.time.LocalDateTime;


@JsonPropertyOrder({"result_id","test_id","total_right_answer","survey_category_answer","user_id","test_date"})
public class TestResultDto {

    @JsonProperty("result_id")
    private int id;

    @JsonProperty("test_id")
    private int testId;

    @JsonProperty("total_right_answer")
    private int totalRightAnswer;

    @JsonProperty("survey_category_answer")
    private String surveyCategoryAnswer;

    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("test_date")
    private LocalDateTime testDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getTotalRightAnswer() {
        return totalRightAnswer;
    }

    public void setTotalRightAnswer(int totalRightAnswer) {
        this.totalRightAnswer = totalRightAnswer;
    }

    public String getSurveyCategoryAnswer() {
        return surveyCategoryAnswer;
    }

    public void setSurveyCategoryAnswer(String surveyCategoryAnswer) {
        this.surveyCategoryAnswer = surveyCategoryAnswer;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocalDateTime getTestDate() {
        return testDate;
    }

    public void setTestDate(LocalDateTime testDate) {
        this.testDate = testDate;
    }
}
