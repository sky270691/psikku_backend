package com.psikku.backend.dto.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.Pattern;
import java.util.List;

@JsonPropertyOrder({"id","internal_name","name","description","is_survey","survey_category","subtests"})
public class FullTestDto {

    private int id;

    @Pattern(regexp = "[^_]+", message = "test name cannot contain '_' (under score) character")
    private String name;

    @JsonProperty("internal_name")
    private String internalName;

    private String description;

    private int skippable;

    @JsonProperty("is_survey")
    private boolean isSurvey;

//    @JsonProperty("survey_category")
//    private List<SurveyCategoryDto> surveyCategoryDto;

//    @JsonProperty(value = "subtest")

    private List<SubtestDto> subtests;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsSurvey() {
        return isSurvey;
    }

    public void setIsSurvey(boolean isSurvey) {
        this.isSurvey = isSurvey;
    }

//    public List<SurveyCategoryDto> getSurveyCategoryDto() {
//        return surveyCategoryDto;
//    }
//
//    public void setSurveyCategoryDto(List<SurveyCategoryDto> surveyCategoryDto) {
//        this.surveyCategoryDto = surveyCategoryDto;
//    }

    public List<SubtestDto> getSubtests() {
        return subtests;
    }

    public void setSubtests(List<SubtestDto> subtests) {
        this.subtests = subtests;
    }

    public boolean isSurvey() {
        return isSurvey;
    }

    public void setSurvey(boolean survey) {
        isSurvey = survey;
    }

    public int getSkippable() {
        return skippable;
    }

    public void setSkippable(int skippable) {
        this.skippable = skippable;
    }
}
