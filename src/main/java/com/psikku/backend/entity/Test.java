package com.psikku.backend.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "test")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "internal_name")
    private String internalName;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_survey")
    private boolean isSurvey;

    @Column(name = "view")
    private boolean view;

    private int skippable;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "test_id")
    private List<Subtest> subtestList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "test_id")
    private List<SurveyCategory> surveyCategoryList;

    @OneToMany(mappedBy = "test",fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH,CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
    private List<TestPackageTest> testPackageTestList;

    @OneToMany(mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<TestResult> testResultList;

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

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public List<Subtest> getSubtestList() {
        return subtestList;
    }

    public void setSubtestList(List<Subtest> subtestList) {
        this.subtestList = subtestList;
    }

    public boolean isSurvey() {
        return isSurvey;
    }

    public void setSurvey(boolean survey) {
        isSurvey = survey;
    }

    public List<TestPackageTest> getTestPackageTestList() {
        return testPackageTestList;
    }

    public void setTestPackageTestList(List<TestPackageTest> testPackageTestList) {
        this.testPackageTestList = testPackageTestList;
    }

    public List<SurveyCategory> getSurveyCategoryList() {
        return surveyCategoryList;
    }

    public void setSurveyCategoryList(List<SurveyCategory> surveyCategoryList) {
        this.surveyCategoryList = surveyCategoryList;
    }

    public List<TestResult> getTestResultList() {
        return testResultList;
    }

    public void setTestResultList(List<TestResult> testResultList) {
        this.testResultList = testResultList;
    }

    public int getSkippable() {
        return skippable;
    }

    public void setSkippable(int skippable) {
        this.skippable = skippable;
    }
}
