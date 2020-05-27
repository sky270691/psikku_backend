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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "test_id")
    private List<Subtest> subtestList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "test_id")
    private List<SurveyCategory> surveyCategoryList;

    @ManyToMany(mappedBy = "testList",fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH,CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
    private List<TestPackage> testPackageList;

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

    public List<TestPackage> getTestPackageList() {
        return testPackageList;
    }

    public void setTestPackageList(List<TestPackage> testPackageList) {
        this.testPackageList = testPackageList;
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
}
