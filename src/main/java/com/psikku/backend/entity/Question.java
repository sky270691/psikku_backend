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

//    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id")
    private List<Answer> answersList;

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

    public List<Answer> getAnswersList() {
        return answersList;
    }

    public void setAnswersList(List<Answer> answersList) {
        this.answersList = answersList;
    }

//    public Subtest getSubtest() {
//        return subtest;
//    }
//
//    public void setSubtest(Subtest subtest) {
//        this.subtest = subtest;
//    }
}
