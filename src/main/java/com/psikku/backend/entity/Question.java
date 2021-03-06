package com.psikku.backend.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "question")
public class Question {

    @Id
//    @Column(nullable = false, updatable = false)
    private String id;

    @Column(name = "question_content")
    private String questionContent;

    @Column(name = "question_category")
    private String questionCategory;

//    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id")
    private List<Answer> answerList;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "sub_test_id")
//    private Subtest subtest;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getQuestionCategory() {
        return questionCategory;
    }

    public void setQuestionCategory(String questionCategory) {
        this.questionCategory = questionCategory;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answersList) {
        this.answerList = answersList;
    }

//    public Subtest getSubtest() {
//        return subtest;
//    }
//
//    public void setSubtest(Subtest subtest) {
//        this.subtest = subtest;
//    }
}
