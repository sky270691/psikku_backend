package com.psikku.backend.service.testresult;

import com.psikku.backend.dto.testresult.TestFinalResultDto;
import com.psikku.backend.dto.testresult.TestResultDto;
import com.psikku.backend.entity.TestResult;

import java.time.LocalDateTime;
import java.util.List;

public interface TestResultService  {
    //--------------------repository method-----------------------------------------
    boolean saveTestResult(TestResult testResult);
    List<TestResult> findAllByUserName (String username);
    List<TestResult> findAllResultByUserId(long userId);
    List<TestResult> findAllResultByTestId(int id);

    //-------------converter method-------------------------------------------------
    TestFinalResultDto convertToTestResultDto(TestResult testResult);
}
