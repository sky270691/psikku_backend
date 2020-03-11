package com.psikku.backend.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "question")
public class Question {

    @Id
//    @Column(nullable = false, updatable = false)
    private String id;

    @Column(name = "question_content_1")
    private String questionContent1;

    @Column(name = "question_content_2")
    private String questionContent2;

    @Column(name = "question_content_3")
    private String questionContent3;

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

    public String getQuestionContent1() {
        return questionContent1;
    }

    public void setQuestionContent1(String questionContent1) {
        this.questionContent1 = questionContent1;
    }

    public String getQuestionContent2() {
        return questionContent2;
    }

    public void setQuestionContent2(String questionContent2) {
        this.questionContent2 = questionContent2;
    }

    public String getQuestionContent3() {
        return questionContent3;
    }

    public void setQuestionContent3(String questionContent3) {
        this.questionContent3 = questionContent3;
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
