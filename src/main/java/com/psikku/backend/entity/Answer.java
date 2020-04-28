package com.psikku.backend.entity;

import javax.persistence.*;

@Entity
@Table(name = "answer")
public class Answer {

    @Id
//    @Column(updatable = false, nullable = false)
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Column(name = "answer_content")
    private String answerContent;

    @Column(name = "is_correct")
    private int isCorrect;

    @Column(name = "answer_category")
    private String answerCategory;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "question_id")
//    private Question question;

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(int isCorrect) {
        this.isCorrect = isCorrect;
    }

    public String getAnswerCategory() {
        return answerCategory;
    }

    public void setAnswerCategory(String answerCategory) {
        this.answerCategory = answerCategory;
    }

    //    public Question getQuestion() {
//        return question;
//    }
//
//    public void setQuestion(Question question) {
//        this.question = question;
//    }
}
