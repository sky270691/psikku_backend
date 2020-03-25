package com.psikku.backend.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_result")
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "test_id")
    private Test test;

    @ManyToOne(fetch = FetchType.EAGER,
                cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    private int totalRightAnswer;

    private String surveyCategoryAnswer;


    @CreationTimestamp
    @Column(name = "creation_date")
    private LocalDateTime dateOfTest;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
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

    public LocalDateTime getDateOfTest() {
        return dateOfTest;
    }

    public void setDateOfTest(LocalDateTime dateOfTest) {
        this.dateOfTest = dateOfTest;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
