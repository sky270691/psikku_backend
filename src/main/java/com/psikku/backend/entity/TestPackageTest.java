package com.psikku.backend.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.psikku.backend.entity.embeddedkey.TestPackageTestPK;

import javax.persistence.*;

@Entity
@Table(name = "package_test")
public class TestPackageTest {

    @EmbeddedId
    private TestPackageTestPK id = new TestPackageTestPK();

    @ManyToOne
    @MapsId("testPackageId")
    @JoinColumn(name = "package_id")
    private TestPackage testPackage;

    @ManyToOne
    @MapsId("testId")
    @JoinColumn(name = "test_id")
    private Test test;

    private int priority;

    @Column(name = "take_pict")
    private boolean takePict;


    public TestPackageTestPK getId() {
        return id;
    }

    public void setId(TestPackageTestPK id) {
        this.id = id;
    }

    public TestPackage getTestPackage() {
        return testPackage;
    }

    public void setTestPackage(TestPackage testPackage) {
        this.testPackage = testPackage;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isTakePict() {
        return takePict;
    }

    public void setTakePict(boolean takePict) {
        this.takePict = takePict;
    }
}
