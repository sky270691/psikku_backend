package com.psikku.backend.service;

import com.psikku.backend.entity.TestPackage;
import com.psikku.backend.entity.Test;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TestPackageService {

    boolean generatePackage(List<Test> tests);

    TestPackage getPackageById(int packageId);

    List<Test> getAllTestInPackage (TestPackage testPackage);

    void savePackage(TestPackage testPackage);

    void deletePackage(TestPackage testPackage);
}
