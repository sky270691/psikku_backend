package com.psikku.backend.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "answer")
public class Answer {

    private int id;
    private String answerContent;
    private int isCorrect;


}
