package com.psikku.backend.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "question_content")
    private String questionContent;

    @OneToMany
    @JoinColumn(name = "test_question_id")
    private List<Answer> answersList;

}
