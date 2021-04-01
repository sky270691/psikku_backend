package com.psikku.backend.entity.embeddedkey;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class TestPackageTestPK implements Serializable {

    @Column(name = "package_id")
    private int testPackageId;

    @Column(name = "test_id")
    private int testId;


    public int getTestPackageId() {
        return testPackageId;
    }

    public void setTestPackageId(int testPackageId) {
        this.testPackageId = testPackageId;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }
}
