package com.psikku.backend.service;

import com.psikku.backend.entity.TestResult;
import com.psikku.backend.repository.TestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestResultServiceImpl implements TestResultService {

    @Autowired
    TestResultRepository testResultRepository;

    @Override
    public boolean saveTestResult(TestResult testResult) {
        testResultRepository.save(testResult);
        return true;
    }
}
