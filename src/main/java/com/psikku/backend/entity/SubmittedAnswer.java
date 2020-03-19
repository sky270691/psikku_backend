package com.psikku.backend.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "submitted_answer")
public class SubmittedAnswer {

    @Id
    private String id;

    @Column(name = "answer_1")
    private String answer1;

    @Column(name = "answer_2")
    private String answer2;

    @Column(name = "answer_3")
    private String answer3;

    @CreationTimestamp
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST,CascadeType.DETACH})
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST,CascadeType.DETACH})
    @JoinColumn(name = "question_id")
    private Question question;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
