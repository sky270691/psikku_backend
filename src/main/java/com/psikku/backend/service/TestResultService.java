package com.psikku.backend.service;

import com.psikku.backend.dto.testresult.TestResultDto;
import com.psikku.backend.entity.TestResult;

import java.time.LocalDateTime;
import java.util.List;

public interface TestResultService  {
    //--------------------repository method-----------------------------------------
    boolean saveTestResult(TestResult testResult);
    List<TestResult> findAllResultByUserId(long userId);
    List<TestResult> findAllResultByUserIdAndDateOfTest(long userId, String date, String time);

    //-------------converter method-------------------------------------------------
    TestResultDto convertToTestResultDto(TestResult testResult);
}
