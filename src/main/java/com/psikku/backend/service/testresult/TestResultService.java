package com.psikku.backend.service.testresult;

import com.psikku.backend.dto.testresult.TestFinalResultDto;
import com.psikku.backend.entity.TestResult;

import java.util.List;

public interface TestResultService  {
    //--------------------repository method-----------------------------------------
    boolean saveTestResult(TestResult testResult);
    List<TestResult> findAllByUserName (String username);
    List<TestResult> findAllResultByUserId(long userId);
    List<TestResult> findAllResultByTestId(int id);
    List<TestResult> findAllResultByVoucherIdAndUsername(long voucherId, String userName);
    List<TestResult> findAllResultByTestIdAndVoucherId(int id, long voucherId);

    //-------------converter method-------------------------------------------------
    TestFinalResultDto convertToTestResultDto(TestResult testResult);
}
