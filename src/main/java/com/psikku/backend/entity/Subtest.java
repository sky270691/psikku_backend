package com.psikku.backend.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "subtest")
public class Subtest {

    @Id
    private String id;

    @Column(name = "guide")
    private String guide;

    @Column(name = "test_type")
    private String test_type;

    @OneToMany
    @JoinColumn(name = "sub_test_id")
    private List<Question> questionList;


}
