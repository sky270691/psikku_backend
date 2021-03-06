package com.psikku.backend.entity;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "subtest")
public class Subtest {

    @Id
//    @Column(updatable = false,nullable = false)
    private String id;

    @Column(name = "guide")
    private String guide;

    @Column(name = "test_type")
    private String testType;

    @Column(name = "duration")
    private int duration;

//    @OneToMany(mappedBy = "subtest", cascade = CascadeType.ALL)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "sub_test_id")
    private List<Question> questionList;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "test_id")
//    private test test;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
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

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

//    public test getTest() {
//        return test;
//    }
//
//    public void setTest(test test) {
//        this.test = test;
//    }
}
