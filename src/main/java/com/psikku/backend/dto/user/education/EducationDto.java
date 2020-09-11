package com.psikku.backend.dto.user.education;


public class EducationDto {

    private Long id;
    private String educationLevel;
    private String institutionName;
    private Integer graduatedYear;
    private String major;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public Integer getGraduatedYear() {
        return graduatedYear;
    }

    public void setGraduatedYear(Integer graduatedYear) {
        this.graduatedYear = graduatedYear;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
